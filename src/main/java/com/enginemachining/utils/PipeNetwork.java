package com.enginemachining.utils;

import com.enginemachining.EngineMachiningMod;
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
    private class PipeTraceEntry {
        IPipeTraceable pipe;
        int useCount;
    }

    // Stores all pipes in the network
    private final Set<IPipeTraceable> pipes;
    // Stores all receivers in the network
    private final Set<IPipeReceiver> receivers;
    // Stores all senders and the pipes they use to get to every receiver
    private final Set<Pair<IPipeSender, Map<BlockPos, Integer>>> senders;

    // Stores the level in which this network exists
    private final World level;

    private final Class<? extends IPipeTraceable> pipeClass;
    private final Class<? extends IPipeReceiver> receiverClass;
    private final Class<? extends IPipeSender> senderClass;
    private final Capability<?> capability;

    public PipeNetwork(World level, Class<? extends IPipeTraceable> pipeClass, Class<? extends IPipeReceiver> receiverClass, Class<? extends IPipeSender> senderClass, Capability<?> capability) {
        pipes = new HashSet<>();
        receivers = new HashSet<>();
        senders = new HashSet<>();

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
        if (pipeClass.isInstance(te)) pipes.add(traceable);
        else if(receiverClass.isInstance(te)) receivers.add((IPipeReceiver) te);
        else if(senderClass.isInstance(te)) senders.add(new Pair<>((IPipeSender) te, null));

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
                    pipes.add(ntraceable);
                    traceNetwork(neighbour, traced);
                } else if(receiverClass.isInstance(nte)) {
                    receivers.add((IPipeReceiver) nte);
                } else if(senderClass.isInstance(nte)) {
                    senders.add(new Pair<>((IPipeSender) nte, null));
                }
            }
        }
    }

    public Set<IPipeReceiver> getReceivers() {
        return receivers;
    }

    public void dump() {
        Logger log = EngineMachiningMod.LOGGER;
        log.debug("Pipes (" + pipes.size() + "): ");
        for(IPipeTraceable p : pipes) {
            log.debug("\t" + ((TileEntity)p).getBlockPos());
        }
        log.debug("Receivers (" + receivers.size() + "): ");
        for(IPipeReceiver r : receivers) {
            log.debug("\t" + ((TileEntity)r).getBlockPos());
        }
        log.debug("Senders (" + senders.size() + "): ");
        for(Pair<IPipeSender, Map<BlockPos, Integer>> s : senders) {
            log.debug("\t" + ((TileEntity)s.getFirst()).getBlockPos());
        }
    }
}
