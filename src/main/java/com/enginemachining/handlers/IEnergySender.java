package com.enginemachining.handlers;

import com.enginemachining.utils.IPipeTraceable;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.energy.IEnergyStorage;

public interface IEnergySender extends IPipeTraceable {
    IEnergyStorage getHandler();

    TileEntity getTileEntity();
}
