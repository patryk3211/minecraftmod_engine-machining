package com.enginemachining.handlers;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.energy.IEnergyStorage;

public interface IEnergySender {
    IEnergyStorage getHandler();

    TileEntity getTileEntity();
}
