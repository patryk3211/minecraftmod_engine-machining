package com.enginemachining.utils;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public interface IPipeTraceable {
    enum Type {
        NONE,
        PIPE,
        RECEIVER,
        SENDER,
        TRANSCEIVER
    }

    PipeNetwork getNetwork(@Nullable Direction side);
    void setNetwork(@Nullable Direction side, PipeNetwork network);
    void removeNetwork(PipeNetwork network);
    void replaceNetwork(PipeNetwork oldNetwork, PipeNetwork newNetwork);

    boolean canConnect(Direction side, Capability<?> capability);

    Type getSideType(Direction side, Capability<?> capability);
    Type getMainType(Capability<?> capability);

    BlockPos getBlockPosition();
    World getWorld();

    float getResistance();
}
