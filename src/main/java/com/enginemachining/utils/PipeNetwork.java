package com.enginemachining.utils;

import com.enginemachining.EngineMachiningMod;
import com.enginemachining.api.ITrackableHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import org.apache.logging.log4j.Logger;

import java.nio.IntBuffer;
import java.util.*;
import java.util.function.Supplier;

public class PipeNetwork {
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

    private final Capability<? extends ITrackableHandler> capability;

    protected PipeNetwork(World level, Capability<? extends ITrackableHandler> capability) {
        pipes = new HashMap<>();
        receivers = new HashMap<>();
        senders = new HashMap<>();

        this.capability = capability;
        this.level = level;
    }

    public static void removeTraceable(BlockPos traceablePos, World level, Capability<? extends ITrackableHandler> capability, PipeNetwork network, Supplier<? extends PipeNetwork> supplier) {
        if(network != null) network.delete();
        for(Direction d : Direction.values()) {
            BlockPos neighbourPos = traceablePos.offset(d.getNormal());
            TileEntity nte = level.getBlockEntity(neighbourPos);
            if(!(nte instanceof IPipeTraceable)) continue;
            IPipeTraceable neighbour = (IPipeTraceable) nte;
            // TODO: [12.06.2021] Maybe don't recreate the entire network when one wire is deleted.
            if(neighbour.getNetwork() != null) continue;
            PipeNetwork net = supplier.get();//new PipeNetwork(neighbour.getLevel(), capability);
            net.traceNetwork(neighbourPos);
            net.traceReceivers();
        }
    }

    public void delete() {
        pipes.forEach((pos, pipe) -> pipe.setNetwork(null));
        receivers.forEach((pos, recPack) -> recPack.receiver.setNetwork(null));
        senders.forEach((pos, send) -> send.setNetwork(null));
    }

    public static void addTraceable(IPipeTraceable traceable, Capability<? extends ITrackableHandler> capability, Supplier<? extends PipeNetwork> supplier) {
        Map<PipeNetwork, int[]> networks = new HashMap<>();
        for(Direction d : Direction.values()) {
            if(!traceable.canConnect(d, capability)) continue;
            BlockPos neighbourPos = traceable.getBlockPos().offset(d.getNormal());
            TileEntity nte = traceable.getLevel().getBlockEntity(neighbourPos);
            if(!(nte instanceof IPipeTraceable)) continue;
            IPipeTraceable neighbour = (IPipeTraceable) nte;
            if(!neighbour.canConnect(d.getOpposite(), capability)) continue;
            PipeNetwork net = neighbour.getNetwork();
            if(net == null) continue;
            if(net.capability != capability) continue;
            if(networks.containsKey(net)) networks.get(net)[0]++;
            else {
                int[] buffer = new int[1];
                buffer[0] = 1;
                networks.put(net, buffer);
            }
        }
        IPipeTraceable.Type type = traceable.getMainType(capability);
        if(networks.size() == 0) {
            // Create a new network.
            PipeNetwork network = supplier.get();//new PipeNetwork(traceable.getLevel(), capability);
            network.traceNetwork(traceable.getBlockPos());
            network.dump();
            network.traceReceivers();
        } else if(networks.size() == 1) {
            // Only one network found around the traceable
            networks.forEach((net, useCount) -> {
                traceable.setNetwork(net);
                if(useCount[0] == 1) {
                    if(type == IPipeTraceable.Type.PIPE) net.pipes.put(traceable.getBlockPos(), traceable); // We don't need to retrace a blind pipe
                    else {
                        // For a sender or a receiver we need to retrace every receiver.
                        if(type == IPipeTraceable.Type.RECEIVER) net.receivers.put(traceable.getBlockPos(), new ReceiverPacket(traceable));
                        else if(type == IPipeTraceable.Type.SENDER) net.senders.put(traceable.getBlockPos(), traceable);
                        net.traceReceivers();
                    }
                    net.dump();
                } else {
                    // This traceable connects to more than one point in the network. We need to retrace all receivers.
                    switch(type) {
                        case PIPE:
                            net.pipes.put(traceable.getBlockPos(), traceable);
                            break;
                        case RECEIVER:
                            net.receivers.put(traceable.getBlockPos(), new ReceiverPacket(traceable));
                            break;
                        case SENDER:
                            net.senders.put(traceable.getBlockPos(), traceable);
                            break;
                    }
                    net.dump();
                    net.traceReceivers();
                }
            });
        } else {
            // More than one network found around the traceable.
            final PipeNetwork[] largestNetwork = {null};
            // Get the biggest network
            networks.forEach((net, use) -> {
                if(largestNetwork[0] == null) {
                    largestNetwork[0] = net;
                    return;
                }
                if(net.getNetworkSize() > largestNetwork[0].getNetworkSize()) largestNetwork[0] = net;
            });
            networks.remove(largestNetwork[0]);
            // Merge other networks into this network
            networks.forEach((net, use) -> {
                net.delete();
                net.pipes.forEach((pos, pipe) -> {
                    pipe.setNetwork(largestNetwork[0]);
                });
                net.receivers.forEach((pos, rec) -> {
                    rec.receiver.setNetwork(largestNetwork[0]);
                });
                net.senders.forEach((pos, sen) -> {
                    sen.setNetwork(largestNetwork[0]);
                });
                largestNetwork[0].pipes.putAll(net.pipes);
                largestNetwork[0].receivers.putAll(net.receivers);
                largestNetwork[0].senders.putAll(net.senders);
            });

            traceable.setNetwork(largestNetwork[0]);
            switch(type) {
                case PIPE:
                    largestNetwork[0].pipes.put(traceable.getBlockPos(), traceable);
                    break;
                case RECEIVER:
                    largestNetwork[0].receivers.put(traceable.getBlockPos(), new ReceiverPacket(traceable));
                    break;
                case SENDER:
                    largestNetwork[0].senders.put(traceable.getBlockPos(), traceable);
                    break;
            }

            largestNetwork[0].dump();
            largestNetwork[0].traceReceivers();
        }
    }

    public int getNetworkSize() {
        return pipes.size() + receivers.size() + senders.size();
    }

    public void traceNetwork(BlockPos traceStart) {
        pipes.clear();
        receivers.clear();
        senders.clear();

        Set<IPipeTraceable> traced = new HashSet<>();

        TileEntity te = level.getBlockEntity(traceStart);
        if(!(te instanceof IPipeTraceable)) return;
        IPipeTraceable traceable = (IPipeTraceable) te;
        //PipeNetwork oldNet = traceable.getNetwork();
        //traceable.setNetwork(this);
        traced.add(traceable);
        IPipeTraceable.Type type = traceable.getMainType(capability);
        switch(type) {
            case PIPE:
                pipes.put(traceStart, traceable);
                //(oldNet != null) oldNet.pipes.remove(traceStart);
                break;
            case RECEIVER:
                receivers.put(traceStart, new ReceiverPacket(traceable));
                //if(oldNet != null) oldNet.receivers.remove(traceStart);
                break;
            case SENDER:
                senders.put(traceStart, traceable);
                //if(oldNet != null) oldNet.senders.remove(traceStart);
                break;
        }
        //if(oldNet != null && oldNet.senders.size() == 0 && oldNet.receivers.size() == 0 && oldNet.pipes.size() == 0) oldNet.delete();*/

        traceNetwork(traceStart, traced);

        dump();
    }

    private void traceNetwork(BlockPos posToTrace, Set<IPipeTraceable> traced) {
        TileEntity te = level.getBlockEntity(posToTrace);
        if(!(te instanceof IPipeTraceable)) return;
        IPipeTraceable traceable = (IPipeTraceable) te;
        PipeNetwork oldNet = ((IPipeTraceable) te).getNetwork();
        ((IPipeTraceable) te).setNetwork(this);
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
            if(oldNet.senders.size() == 0 && oldNet.receivers.size() == 0 && oldNet.pipes.size() == 0) oldNet.delete();
        }
        for(Direction d : Direction.values()) {
            BlockPos neighbour = posToTrace.offset(d.getNormal());
            TileEntity nte = level.getBlockEntity(neighbour);
            if(!(nte instanceof IPipeTraceable)) continue;
            IPipeTraceable ntraceable = (IPipeTraceable) nte;
            if(!traceable.canConnect(d, capability) || !ntraceable.canConnect(d.getOpposite(), capability)) continue;
            if(traced.add(ntraceable)) {
                IPipeTraceable.Type type = ntraceable.getSideType(d.getOpposite(), capability);
                switch (type) {
                    case PIPE:
                        pipes.put(neighbour, ntraceable);
                        traceNetwork(neighbour, traced);
                        break;
                    case RECEIVER:
                        receivers.put(neighbour, new ReceiverPacket(ntraceable));
                        traceNetwork(neighbour, traced);
                        break;
                    case SENDER:
                        senders.put(neighbour, ntraceable);
                        traceNetwork(neighbour, traced);
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
            List<IPipeTraceable> path = new ArrayList<>();
            path.add(rec.receiver);
            for(Direction d : Direction.values()) traceForSender(pos.offset(d.getNormal()), path, 0, pos);
        });
    }

    private void traceForSender(BlockPos position, List<IPipeTraceable> pipes, float resistance, BlockPos receiver) {
        IPipeTraceable pipe = this.pipes.get(position);
        if(pipe == null && this.receivers.containsKey(position)) pipe = this.receivers.get(position).receiver;
        if(pipe == null) pipe = this.senders.get(position);
        if(pipe == null) return;
        //if(!pipes.add(pipe)) return;
        if(pipes.contains(pipe)) return;
        pipes.add(pipe);
        for(Direction dir : Direction.values()) {
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

                list.combinedResistance += resistance + pipe.getResistance();
                ReceiverToSenderPathListEntry pathListEntry = new ReceiverToSenderPathListEntry(resistance + pipe.getResistance(), pipes);
                list.paths.add(pathListEntry);

                /*Pair<IPipeTraceable, Pair<Float, List<Pair<Float, Set<IPipeTraceable>>>>> receiverPacket = receivers.get(receiver);
                if(receiverPacket == null) throw new IllegalStateException("Block Position passed gave a null receiver packet");
                if(receiverPacket.getValue() == null) receiverPacket.setValue(new Pair<>(0f, null));
                if(receiverPacket.getValue().getValue() == null) receiverPacket.getValue().setValue(new ArrayList<>());
                // Refresh the combined resistance
                float currentCombinedResistance = receiverPacket.getValue().getKey();
                currentCombinedResistance += resistance + pipe.getResistance();
                receiverPacket.getValue().setKey(currentCombinedResistance);

                Pair<Float, Set<IPipeTraceable>> pathEntry = new Pair<>(resistance + pipe.getResistance(), pipes);
                receiverPacket.getValue().getValue().add(pathEntry);*/

                System.out.println("Sender: " + neighbourPos);
                for(IPipeTraceable p : pipes) {
                    System.out.println("\t" + p.getBlockPos());
                }
                continue;
            }

            // We trace any other pipes we find
            IPipeTraceable neighbour = this.pipes.get(neighbourPos);
            if(neighbour == null && this.receivers.containsKey(neighbourPos)) neighbour = this.receivers.get(neighbourPos).receiver;
            if(neighbour == null) neighbour = this.senders.get(neighbourPos);
            if(neighbour == null) continue;
            if(pipes.contains(neighbour)) continue;
            boolean canConnect = neighbour.canConnect(dir.getOpposite(), capability);
            if(canConnect) {
                List<IPipeTraceable> copy = new ArrayList<>(pipes);
                traceForSender(neighbourPos, copy, resistance + pipe.getResistance(), receiver);
            }
        }
        /*if(!pipeDetected) {
            for(Direction d : Direction.values()) {
                BlockPos neighbourPos = position.offset(d.getNormal());
                IPipeTraceable neighbour = null;
                if(this.receivers.containsKey(neighbourPos)) neighbour = this.receivers.get(neighbourPos).receiver;
                else this.senders.get(neighbourPos);
                if(neighbour == null) continue;
                if(neighbour.canConnect(d.getOpposite(), capability)) {
                    List<IPipeTraceable> copy = new ArrayList<>(pipes);
                    traceForSender(neighbourPos, copy, resistance + pipe.getResistance(), receiver);
                }
            }
        }*/
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
