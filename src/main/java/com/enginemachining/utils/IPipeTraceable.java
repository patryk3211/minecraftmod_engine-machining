package com.enginemachining.utils;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

public interface IPipeTraceable {
    enum Type {
        NONE,
        PIPE,
        RECEIVER,
        SENDER,
        TRANSCEIVER
    }

    PipeNetwork getNetwork();
    void setNetwork(PipeNetwork network);

    boolean canConnect(Direction side, Capability<?> capability);

    Type getSideType(Direction side);

    BlockPos getPosition();

    float getResistance();
}
