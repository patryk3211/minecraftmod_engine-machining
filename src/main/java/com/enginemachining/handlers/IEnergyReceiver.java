package com.enginemachining.handlers;

import com.enginemachining.utils.IPipeReceiver;
import net.minecraftforge.energy.IEnergyStorage;

public interface IEnergyReceiver extends IPipeReceiver {
    IEnergyStorage getHandler();
}
