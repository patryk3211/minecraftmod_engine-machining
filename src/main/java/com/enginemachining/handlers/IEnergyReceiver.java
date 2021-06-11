package com.enginemachining.handlers;

import com.enginemachining.api.energy.IEnergyHandler;
import com.enginemachining.utils.IPipeTraceable;

public interface IEnergyReceiver extends IPipeTraceable {
    IEnergyHandler getHandler();
}
