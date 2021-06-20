package com.enginemachining.utils;

import com.enginemachining.EngineMachiningMod;
import com.enginemachining.api.ITrackableHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.ArrayUtils;
import org.apache.logging.log4j.core.util.SetUtils;

import java.util.*;
import java.util.function.Supplier;

public class PipeNetwork {
    private static final int MAX_PIPE_USE_COUNT = 4;
    private static final int MAX_SENDER_PATH_COUNT = 4;
    private static final boolean DEBUG_DUMP = false;

    protected static class ReceiverPacket {
        public IPipeTraceable receiver;
        public Map<IPipeTraceable, ReceiverToSenderPathList> pathsList;

        public ReceiverPacket(IPipeTraceable receiver) {
            this.receiver = receiver;
            this.pathsList = new HashMap<>();
        }

        public void clear() {
            pathsList.clear();
        }
    }

    protected static class ReceiverToSenderPathList {
        public float combinedResistance;
        public List<ReceiverToSenderPathListEntry> paths;

        public ReceiverToSenderPathList() {
            this.combinedResistance = 0;
            this.paths = new ArrayList<>();
        }
    }

    protected static class ReceiverToSenderPathListEntry {
        public float pathResistance;
        public List<IPipeTraceable> pipes;

        public ReceiverToSenderPathListEntry(float pathResistance, List<IPipeTraceable> pipes) {
            this.pathResistance = pathResistance;
            this.pipes = pipes;
        }
    }

    protected static class Pair<T, V> {
        private T key;
        private V value;

        public Pair(T key, V value) {
            this.key = key;
            this.value = value;
        }

        public T getKey() {
            return key;
        }

        public void setKey(T key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }

    // Stores all pipes in the network
    protected final Map<BlockPos, IPipeTraceable> pipes;
    // Stores all receivers in the network
    //                            Pair of Receiver and List of Pair of Combined Resistance and List of Pair of Path Resistance and Set of Pipes
    protected final Map<BlockPos, ReceiverPacket> receivers;
    // Stores all senders and the pipes they use to get to every receiver
    protected final Map<BlockPos, IPipeTraceable> senders;

    // Stores the level in which this network exists
    private final World level;

    private final Capability<?> capability;

    protected PipeNetwork(World level, Capability<?> capability) {
        pipes = new HashMap<>();
        receivers = new HashMap<>();
        senders = new HashMap<>();

        this.capability = capability;
        this.level = level;
    }

    public static void removeTraceable(BlockPos traceablePos, World level, Capability<?> capability, PipeNetwork network, Supplier<? extends PipeNetwork> supplier, Direction dir) {
        if(network != null) network.delete();
        if(dir == null) {
            for (Direction d : Direction.values()) {
                BlockPos neighbourPos = traceablePos.offset(d.getNormal());
                TileEntity nte = level.getBlockEntity(neighbourPos);
                if (!(nte instanceof IPipeTraceable)) continue;
                IPipeTraceable neighbour = (IPipeTraceable) nte;
                if(!neighbour.canConnect(d.getOpposite(), capability)) continue;
                // TODO: [12.06.2021] Maybe don't recreate the entire network when one wire is deleted.
                if (neighbour.getNetwork(d.getOpposite()) != null) continue;
                trace(neighbourPos, level, capability, supplier);
            }
        } else {
            BlockPos neighbourPos = traceablePos.offset(dir.getNormal());
            TileEntity nte = level.getBlockEntity(neighbourPos);
            if (!(nte instanceof IPipeTraceable)) return;
            IPipeTraceable neighbour = (IPipeTraceable) nte;
            // TODO: [12.06.2021] Maybe don't recreate the entire network when one wire is deleted.
            if (neighbour.getNetwork(dir.getOpposite()) != null) return;
            trace(neighbourPos, level, capability, supplier);
        }
    }

    public void delete() {
        pipes.forEach((pos, pipe) -> pipe.removeNetwork(this));
        receivers.forEach((pos, recPack) -> recPack.receiver.removeNetwork(this));
        senders.forEach((pos, send) -> send.removeNetwork(this));
    }

    public static void addTraceable(IPipeTraceable traceable, Capability<?> capability, Supplier<? extends PipeNetwork> supplier) {
        Map<PipeNetwork, Set<Direction>> networks = new HashMap<>();
        for(Direction d : Direction.values()) {
            if(!traceable.canConnect(d, capability)) continue;
            BlockPos neighbourPos = traceable.getBlockPosition().offset(d.getNormal());
            TileEntity nte = traceable.getWorld().getBlockEntity(neighbourPos);
            if(!(nte instanceof IPipeTraceable)) continue;
            IPipeTraceable neighbour = (IPipeTraceable) nte;
            if(!neighbour.canConnect(d.getOpposite(), capability)) continue;
            PipeNetwork net = neighbour.getNetwork(d.getOpposite());
            if(net == null) continue;
            if(net.capability != capability) continue;
            if(networks.containsKey(net)) networks.get(net).add(d);
            else {
                Set<Direction> s = new HashSet<>();
                s.add(d);
                networks.put(net, s);
            }
        }
        IPipeTraceable.Type type = traceable.getMainType(capability);
        if(networks.size() == 0) {
            // Create a new network.
            trace(traceable.getBlockPosition(), traceable.getWorld(), capability, supplier);
        } else if(networks.size() == 1) {
            // Only one network found around the traceable
            networks.forEach((net, dirs) -> {
                dirs.forEach(dir -> traceable.setNetwork(dir, net));
                if(dirs.size() == 1) {
                    if(type == IPipeTraceable.Type.PIPE) net.pipes.put(traceable.getBlockPosition(), traceable); // We don't need to retrace a blind pipe
                    else if(type == IPipeTraceable.Type.TRANSCEIVER) {
                        for (Direction dir : dirs) {
                            IPipeTraceable.Type sideType = traceable.getSideType(dir, capability);
                            if(sideType == IPipeTraceable.Type.RECEIVER) net.receivers.put(traceable.getBlockPosition(), new ReceiverPacket(traceable));
                            else if(sideType == IPipeTraceable.Type.SENDER) net.senders.put(traceable.getBlockPosition(), traceable);
                        }
                        net.traceReceivers();
                    } else {
                        // For a sender or a receiver we need to retrace every receiver.
                        if(type == IPipeTraceable.Type.RECEIVER) net.receivers.put(traceable.getBlockPosition(), new ReceiverPacket(traceable));
                        else if(type == IPipeTraceable.Type.SENDER) net.senders.put(traceable.getBlockPosition(), traceable);
                        net.traceReceivers();
                    }
                    if(DEBUG_DUMP) net.dump();
                } else {
                    // This traceable connects to more than one point in the network. We need to retrace all receivers.
                    switch(type) {
                        case PIPE:
                            net.pipes.put(traceable.getBlockPosition(), traceable);
                            break;
                        case RECEIVER:
                            net.receivers.put(traceable.getBlockPosition(), new ReceiverPacket(traceable));
                            break;
                        case SENDER:
                            net.senders.put(traceable.getBlockPosition(), traceable);
                            break;
                        case TRANSCEIVER:
                            System.out.println("Transceiver detected!!! (add traceable)");
                            break;
                    }
                    if(DEBUG_DUMP) { System.out.println("Network Dump (addTraceable 1 network):"); net.dump(); }
                    net.traceReceivers();
                }
            });
            if(type == IPipeTraceable.Type.TRANSCEIVER) {
                for(Direction dir : Direction.values()) {
                    if(traceable.canConnect(dir, capability) && traceable.getNetwork(dir) == null) {
                        PipeNetwork network = supplier.get();
                        if(traceable.getSideType(dir, capability) == IPipeTraceable.Type.RECEIVER) network.receivers.put(traceable.getBlockPosition(), new ReceiverPacket(traceable));
                        else if(traceable.getSideType(dir, capability) == IPipeTraceable.Type.SENDER) network.senders.put(traceable.getBlockPosition(), traceable);
                        traceable.setNetwork(dir, network);
                    }
                }
            }
        } else {
            // More than one network found around the traceable.
            if(type != IPipeTraceable.Type.TRANSCEIVER) {
                final PipeNetwork[] largestNetwork = {null};
                // Get the biggest network
                networks.forEach((net, use) -> {
                    if (largestNetwork[0] == null) {
                        largestNetwork[0] = net;
                        return;
                    }
                    if (net.getNetworkSize() > largestNetwork[0].getNetworkSize()) largestNetwork[0] = net;
                });
                networks.remove(largestNetwork[0]);
                // Merge other networks into this network
                networks.forEach((net, use) -> {
                    net.pipes.forEach((pos, pipe) -> pipe.replaceNetwork(net, largestNetwork[0]));
                    net.receivers.forEach((pos, rec) -> rec.receiver.replaceNetwork(net, largestNetwork[0]));
                    net.senders.forEach((pos, sen) -> sen.replaceNetwork(net, largestNetwork[0]));
                    largestNetwork[0].pipes.putAll(net.pipes);
                    largestNetwork[0].receivers.putAll(net.receivers);
                    largestNetwork[0].senders.putAll(net.senders);
                    net.delete();
                });

                traceable.setNetwork(null, largestNetwork[0]);
                switch (type) {
                    case PIPE:
                        largestNetwork[0].pipes.put(traceable.getBlockPosition(), traceable);
                        break;
                    case RECEIVER:
                        largestNetwork[0].receivers.put(traceable.getBlockPosition(), new ReceiverPacket(traceable));
                        break;
                    case SENDER:
                        largestNetwork[0].senders.put(traceable.getBlockPosition(), traceable);
                        break;
                }

                if (DEBUG_DUMP) {
                    System.out.println("Network Dump (addTraceable else !TRANSCEIVER):");
                    largestNetwork[0].dump();
                }
                largestNetwork[0].traceReceivers();
            } else {
                networks.forEach((net, dirs) -> {
                    for (Direction dir : dirs) {
                        IPipeTraceable.Type sideType = traceable.getSideType(dir, capability);
                        switch (sideType) {
                            case RECEIVER:
                                net.receivers.put(traceable.getBlockPosition(), new ReceiverPacket(traceable));
                                break;
                            case SENDER:
                                net.senders.put(traceable.getBlockPosition(), traceable);
                                break;
                        }
                        traceable.setNetwork(dir, net);
                    }
                    if (DEBUG_DUMP) {
                        System.out.println("Network Dump (addTraceable else TRANSCEIVER):");
                        net.dump();
                    }
                    net.traceReceivers();
                });
            }
        }
    }

    public int getNetworkSize() {
        return pipes.size() + receivers.size() + senders.size();
    }

    public static void trace(BlockPos traceStart, World level, Capability<?> cap, Supplier<? extends PipeNetwork> supplier) {
        TileEntity te = level.getBlockEntity(traceStart);
        if(!(te instanceof IPipeTraceable)) return;
        IPipeTraceable traceable = (IPipeTraceable) te;
        if(traceable.getMainType(cap) != IPipeTraceable.Type.TRANSCEIVER) {
            PipeNetwork net = supplier.get();
            net.traceNetwork(traceStart, (Direction) null);
            if(DEBUG_DUMP) { System.out.println("Network Dump (trace !TRANSCEIVER):"); net.dump(); }
            net.traceReceivers();
        } else {
            for(Direction d : Direction.values()) {
                if(traceable.getNetwork(d) != null) traceable.getNetwork(d).delete();
                PipeNetwork net = supplier.get();
                net.traceNetwork(traceStart, d);
                if(DEBUG_DUMP) { System.out.println("Network Dump (trace TRANSCEIVER):"); net.dump(); }
                net.traceReceivers();
            }
        }
    }

    public void traceNetwork(BlockPos traceStart, Direction direction) {
        pipes.clear();
        receivers.clear();
        senders.clear();

        Set<IPipeTraceable> traced = new HashSet<>();

        TileEntity te = level.getBlockEntity(traceStart);
        if(!(te instanceof IPipeTraceable)) return;
        IPipeTraceable traceable = (IPipeTraceable) te;

        traced.add(traceable);
        IPipeTraceable.Type type = traceable.getSideType(direction, capability);
        switch (type) {
            case PIPE:
                pipes.put(traceStart, traceable);
                break;
            case RECEIVER:
                receivers.put(traceStart, new ReceiverPacket(traceable));
                break;
            case SENDER:
                senders.put(traceStart, traceable);
                break;
            case NONE:
                return;
        }

        if(direction != null) {
            PipeNetwork oldNet = ((IPipeTraceable) te).getNetwork(direction);
            traceable.setNetwork(direction, this);
            if(oldNet != null) {
                switch (((IPipeTraceable) te).getSideType(direction, capability)) {
                    case PIPE:
                        oldNet.pipes.remove(traceStart);
                        break;
                    case RECEIVER:
                        oldNet.receivers.remove(traceStart);
                        break;
                    case SENDER:
                        oldNet.senders.remove(traceStart);
                        break;
                }
                if (oldNet.senders.size() == 0 && oldNet.receivers.size() == 0 && oldNet.pipes.size() == 0)
                    oldNet.delete();
            }

            BlockPos npos = traceStart.offset(direction.getNormal());
            TileEntity nte = level.getBlockEntity(npos);
            if(!(nte instanceof IPipeTraceable)) return;
            IPipeTraceable ntraceable = (IPipeTraceable) nte;
            traced.add(ntraceable);
            IPipeTraceable.Type ntype = ntraceable.getSideType(direction.getOpposite(), capability);
            switch (ntype) {
                case PIPE:
                    pipes.put(npos, ntraceable);
                    break;
                case RECEIVER:
                    receivers.put(npos, new ReceiverPacket(ntraceable));
                    break;
                case SENDER:
                    senders.put(npos, ntraceable);
                    break;
                case NONE:
                    return;
            }

            traceNetwork(npos, traced);
        } else traceNetwork(traceStart, traced);
    }

    private void traceNetwork(BlockPos posToTrace, Set<IPipeTraceable> traced) {
        TileEntity te = level.getBlockEntity(posToTrace);
        if(!(te instanceof IPipeTraceable)) return;
        IPipeTraceable traceable = (IPipeTraceable) te;
        if(traceable.getMainType(capability) != IPipeTraceable.Type.TRANSCEIVER) {
            PipeNetwork oldNet = ((IPipeTraceable) te).getNetwork(null);
            ((IPipeTraceable) te).setNetwork(null, this);
            if(oldNet != null) {
                switch (((IPipeTraceable) te).getMainType(capability)) {
                    case PIPE:
                        oldNet.pipes.remove(posToTrace);
                        break;
                    case RECEIVER:
                        oldNet.receivers.remove(posToTrace);
                        break;
                    case SENDER:
                        oldNet.senders.remove(posToTrace);
                        break;
                }
                if (oldNet.senders.size() == 0 && oldNet.receivers.size() == 0 && oldNet.pipes.size() == 0) oldNet.delete();
            }
        }
        for(Direction d : Direction.values()) {
            BlockPos neighbour = posToTrace.offset(d.getNormal());
            TileEntity nte = level.getBlockEntity(neighbour);
            if(!(nte instanceof IPipeTraceable)) continue;
            IPipeTraceable ntraceable = (IPipeTraceable) nte;
            if(!traceable.canConnect(d, capability) || !ntraceable.canConnect(d.getOpposite(), capability)) continue;

            if(traceable.getMainType(capability) == IPipeTraceable.Type.TRANSCEIVER) {
                PipeNetwork oldNet = ((IPipeTraceable) te).getNetwork(d);
                ((IPipeTraceable) te).setNetwork(d, this);
                if(oldNet != null) {
                    switch (((IPipeTraceable) te).getSideType(d, capability)) {
                        case PIPE:
                            oldNet.pipes.remove(posToTrace);
                            break;
                        case RECEIVER:
                            oldNet.receivers.remove(posToTrace);
                            break;
                        case SENDER:
                            oldNet.senders.remove(posToTrace);
                            break;
                    }
                    if (oldNet.senders.size() == 0 && oldNet.receivers.size() == 0 && oldNet.pipes.size() == 0)
                        oldNet.delete();
                }
            }

            if(traced.add(ntraceable)) {
                IPipeTraceable.Type mainType = ntraceable.getMainType(capability);
                IPipeTraceable.Type type = ntraceable.getSideType(d.getOpposite(), capability);
                switch (type) {
                    case PIPE:
                        pipes.put(neighbour, ntraceable);
                        if(mainType != IPipeTraceable.Type.TRANSCEIVER) traceNetwork(neighbour, traced);
                        else ntraceable.setNetwork(d.getOpposite(), this);
                        break;
                    case RECEIVER:
                        receivers.put(neighbour, new ReceiverPacket(ntraceable));
                        if(mainType != IPipeTraceable.Type.TRANSCEIVER) traceNetwork(neighbour, traced);
                        else ntraceable.setNetwork(d.getOpposite(), this);
                        break;
                    case SENDER:
                        senders.put(neighbour, ntraceable);
                        if(mainType != IPipeTraceable.Type.TRANSCEIVER) traceNetwork(neighbour, traced);
                        else ntraceable.setNetwork(d.getOpposite(), this);
                        break;
                }
            }
        }
    }

    // Traces from each receiver into every sender to find possible electricity paths
    // We trace from each receiver to calculate the ratios of powers to transfer through each possible path.
    // It might be slower than tracing from each sender and adding the use count of each pipe, but I hope it will be more realistic.
    public void traceReceivers() {
        // Delete all current sender paths
        receivers.forEach((pos, rec) -> {
            rec.clear();
            Map<IPipeTraceable, Integer> useCount = new HashMap<>();
            boolean pipeDetected = false;
            for(Direction d : Direction.values()) {
                List<IPipeTraceable> path = new ArrayList<>();
                path.add(rec.receiver);
                if(!rec.receiver.canConnect(d, capability)) continue;

                BlockPos neighbourPos = pos.offset(d.getNormal());
                IPipeTraceable neighbour = this.pipes.get(neighbourPos);
                /*if(neighbour == null && this.receivers.containsKey(neighbourPos)) neighbour = this.receivers.get(neighbourPos).receiver;
                if(neighbour == null) neighbour = this.senders.get(neighbourPos);*/
                if(neighbour == null) continue;
                if(!neighbour.canConnect(d.getOpposite(), capability)) continue;
                pipeDetected = true;
                traceForSender(pos.offset(d.getNormal()), path, 0, pos, useCount, false);
            }
            if(!pipeDetected || rec.pathsList.size() == 0) {
                for(Direction d : Direction.values()) {
                    List<IPipeTraceable> path = new ArrayList<>();
                    path.add(rec.receiver);
                    if (!rec.receiver.canConnect(d, capability)) continue;

                    BlockPos neighbourPos = pos.offset(d.getNormal());
                    IPipeTraceable neighbour = this.pipes.get(neighbourPos);;
                    if(neighbour == null && this.receivers.containsKey(neighbourPos)) neighbour = this.receivers.get(neighbourPos).receiver;
                    else if(neighbour == null) neighbour = this.senders.get(neighbourPos);
                    if(neighbour == null) continue;
                    if(!neighbour.canConnect(d.getOpposite(), capability)) continue;
                    traceForSender(pos.offset(d.getNormal()), path, 0, pos, useCount, true);
                }
            }
        });
    }

    private Direction[] orderDirectionsInDistance(BlockPos currentPos, BlockPos receiver) {
        List<Direction> dirs = new ArrayList<>();
        ReceiverPacket rec = receivers.get(receiver);
        for(Direction d : Direction.values()) {
            BlockPos neighbourPos = currentPos.offset(d.getNormal());
            for (BlockPos sen : senders.keySet()) {
                if(neighbourPos.distManhattan(sen) < currentPos.distManhattan(sen)) {
                    if(!rec.pathsList.containsKey(senders.get(sen))) dirs.add(d);
                    break;
                }
            }
        }
        for(Direction d : Direction.values()) {
            if(!dirs.contains(d)) dirs.add(dirs.size(), d);
        }
        return dirs.toArray(new Direction[0]);
    }

    private boolean traceForSender(BlockPos position, List<IPipeTraceable> pipes, float resistance, BlockPos receiver, Map<IPipeTraceable, Integer> useCount, boolean gotThroughNonPipes) {
        IPipeTraceable pipe = this.pipes.get(position);
        if(pipe == null && this.receivers.containsKey(position)) pipe = this.receivers.get(position).receiver;
        if(pipe == null) pipe = this.senders.get(position);
        if(pipe == null) return true;
        //if(!pipes.add(pipe)) return;
        if(pipes.contains(pipe)) return true;
        pipes.add(pipe);
        if(useCount.containsKey(pipe)) {
            int uC = useCount.get(pipe);
            if(uC >= MAX_PIPE_USE_COUNT) return true;
            useCount.replace(pipe, uC+1);
        } else useCount.put(pipe, 1);
        boolean pipeDetected = false;
        for(Direction dir : orderDirectionsInDistance(position, receiver)) {
            if(!pipe.canConnect(dir, capability)) continue;
            BlockPos neighbourPos = position.offset(dir.getNormal());
            IPipeTraceable sender = this.senders.get(neighbourPos);
            if(sender != null) {
                // We found a sender, add the path to it's path list.
                boolean canConnect = sender.canConnect(dir.getOpposite(), capability);
                if(!canConnect) continue;
                ReceiverPacket packet = receivers.get(receiver);
                ReceiverToSenderPathList list = packet.pathsList.get(sender);
                if(list == null) {
                    list = new ReceiverToSenderPathList();
                    packet.pathsList.put(sender, list);
                }
                if(list.paths.size() >= MAX_SENDER_PATH_COUNT) return false;

                list.combinedResistance += resistance + pipe.getResistance();
                ReceiverToSenderPathListEntry pathListEntry = new ReceiverToSenderPathListEntry(resistance + pipe.getResistance(), pipes);
                list.paths.add(pathListEntry);

                if(DEBUG_DUMP) {
                    System.out.println("Sender: " + neighbourPos);
                    for (IPipeTraceable p : pipes) {
                        System.out.println("\t" + p.getBlockPosition());
                    }
                }
                continue;
            }

            // We trace any other pipes we find
            IPipeTraceable neighbour = this.pipes.get(neighbourPos);
            //if(neighbour == null && this.receivers.containsKey(neighbourPos)) neighbour = this.receivers.get(neighbourPos).receiver;
            //if(neighbour == null) neighbour = this.senders.get(neighbourPos);
            if(neighbour == null) continue;
            if(pipes.contains(neighbour)) continue;
            boolean canConnect = neighbour.canConnect(dir.getOpposite(), capability);
            if(canConnect) {
                List<IPipeTraceable> copy = new ArrayList<>(pipes);
                pipeDetected = true;
                boolean shouldContinue = traceForSender(neighbourPos, copy, resistance + pipe.getResistance(), receiver, useCount, gotThroughNonPipes);
                if(!shouldContinue) return false;
            }
        }
        if(gotThroughNonPipes) {
            if (!pipeDetected || this.receivers.get(receiver).pathsList.size() == 0) {
                for (Direction d : Direction.values()) {
                    BlockPos neighbourPos = position.offset(d.getNormal());
                    IPipeTraceable neighbour = null;
                    if (this.receivers.containsKey(neighbourPos)) neighbour = this.receivers.get(neighbourPos).receiver;
                    else this.senders.get(neighbourPos);
                    if (neighbour == null) continue;
                    if (neighbour.canConnect(d.getOpposite(), capability)) {
                        List<IPipeTraceable> copy = new ArrayList<>(pipes);
                        traceForSender(neighbourPos, copy, resistance + pipe.getResistance(), receiver, useCount, true);
                    }
                }
            }
        }
        return true;
    }

    /*public Collection<IPipeTraceable> getReceivers() {
        return receivers.values();
    }*/

    public void dump() {
        Logger log = EngineMachiningMod.LOGGER;
        log.debug("Pipes (" + pipes.size() + "): ");
        pipes.forEach((pos, traceable) -> log.debug("\t" + pos));

        log.debug("Receivers (" + receivers.size() + "): ");
        receivers.forEach((pos, receiver) -> log.debug("\t" + pos));

        log.debug("Senders (" + senders.size() + "): ");
        senders.forEach((pos, sender) -> log.debug("\t" + pos));
    }
}
