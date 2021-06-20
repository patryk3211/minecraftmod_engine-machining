package com.enginemachining.handlers;

import com.enginemachining.api.energy.IEnergyHandler;
import com.enginemachining.utils.IPipeTraceable;

public interface IEnergyHandlerProvider extends IPipeTraceable {
    IEnergyHandler getEnergyHandler();
}
