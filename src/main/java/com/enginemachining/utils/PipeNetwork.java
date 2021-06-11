package com.enginemachining.utils;

import com.enginemachining.EngineMachiningMod;
import com.enginemachining.api.ITrackableHandler;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class PipeNetwork {
    private static class ReceiverPath {
        Set<BlockPos> pipes;
        float resistance;
    }
    private static class ReceiverPathList {
        List<ReceiverPath> paths;
        float combinedResistances;
    }

    // Stores all pipes in the network
    private final Map<BlockPos, IPipeTraceable> pipes;
    // Stores all receivers in the network
    private final Map<BlockPos, IPipeTraceable> receivers;
    // Stores all senders and the pipes they use to get to every receiver
    private final Map<BlockPos, Pair<IPipeTraceable, ReceiverPathList>> senders;

    // Stores the level in which this network exists
    private final World level;

    private final Class<? extends IPipeTraceable> pipeClass;
    private final Class<? extends IPipeTraceable> receiverClass;
    private final Class<? extends IPipeTraceable> senderClass;
    private final Capability<? extends ITrackableHandler> capability;

    public PipeNetwork(World level, Class<? extends IPipeTraceable> pipeClass, Class<? extends IPipeTraceable> receiverClass, Class<? extends IPipeTraceable> senderClass, Capability<? extends ITrackableHandler> capability) {
        pipes = new HashMap<>();
        receivers = new HashMap<>();
        senders = new HashMap<>();

        this.capability = capability;
        this.level = level;
        this.pipeClass = pipeClass;
        this.receiverClass = receiverClass;
        this.senderClass = senderClass;
    }

    public void traceNetwork(BlockPos traceStart) {
        pipes.clear();
        receivers.clear();
        senders.clear();

        Set<IPipeTraceable> traced = new HashSet<>();

        TileEntity te = level.getBlockEntity(traceStart);
        if(!(te instanceof IPipeTraceable)) return;
        IPipeTraceable traceable = (IPipeTraceable) te;
        traceable.setNetwork(this);
        traced.add(traceable);
        if (pipeClass.isInstance(te)) pipes.put(traceStart, traceable);
        else if(receiverClass.isInstance(te)) receivers.put(te.getBlockPos(), (IPipeTraceable) te);
        else if(senderClass.isInstance(te)) senders.put(te.getBlockPos(), new Pair<>((IPipeTraceable) te, null));

        traceNetwork(traceStart, traced);
    }

    private void traceNetwork(BlockPos posToTrace, Set<IPipeTraceable> traced) {
        TileEntity te = level.getBlockEntity(posToTrace);
        if(!(te instanceof IPipeTraceable)) return;
        IPipeTraceable traceable = (IPipeTraceable) te;
        ((IPipeTraceable) te).setNetwork(this);
        for(Direction d : Direction.values()) {
            BlockPos neighbour = posToTrace.offset(d.getNormal());
            TileEntity nte = level.getBlockEntity(neighbour);
            if(!(nte instanceof IPipeTraceable)) continue;
            IPipeTraceable ntraceable = (IPipeTraceable) nte;
            if(!traceable.canConnect(d, capability) || !ntraceable.canConnect(d.getOpposite(), capability)) continue;
            if(traced.add(ntraceable)) {
                if (pipeClass.isInstance(nte)) {
                    pipes.put(neighbour, ntraceable);
                    traceNetwork(neighbour, traced);
                } else if(receiverClass.isInstance(nte)) {
                    receivers.put(nte.getBlockPos(), (IPipeTraceable) nte);
                } else if(senderClass.isInstance(nte)) {
                    senders.put(nte.getBlockPos(), new Pair<>((IPipeTraceable) nte, null));
                }
            }
        }
    }

    // Traces from each receiver into every sender to find possible electricity paths
    // We trace from each receiver to calculate the ratios of powers to transfer through each possible path.
    // It might be slower than tracing from each sender and adding the use count of each pipe, but I hope it will be more realistic.
    public void traceReceivers() {
        receivers.forEach((pos, rec) -> {
            Set<IPipeTraceable> path = new HashSet<>();
            traceForSender(pos, path, 0);
        });
    }

    private void traceForSender(BlockPos position, Set<IPipeTraceable> pipes, float resistance) {
        IPipeTraceable pipe = this.pipes.get(position);
        pipes.add(pipe);
        for(Direction dir : Direction.values()) {
            BlockPos neighbourPos = position.offset(dir.getNormal());

            IPipeTraceable neighbour = this.pipes.get(neighbourPos);

        }
    }

    public Collection<IPipeTraceable> getReceivers() {
        return receivers.values();
    }

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
