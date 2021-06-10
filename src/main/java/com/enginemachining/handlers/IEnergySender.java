package com.enginemachining.handlers;

import com.enginemachining.utils.IPipeSender;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.energy.IEnergyStorage;

public interface IEnergySender extends IPipeSender {
    IEnergyStorage getHandler();

    TileEntity getTileEntity();
}
