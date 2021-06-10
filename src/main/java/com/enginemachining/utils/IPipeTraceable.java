package com.enginemachining.utils;

import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public interface IPipeTraceable {
    PipeNetwork getNetwork();
    void setNetwork(PipeNetwork network);

    boolean canConnect(Direction side, Capability<?> capability);
}
