package com.enginemachining.capabilities;

import com.enginemachining.api.energy.IEnergyHandler;
import com.enginemachining.capabilities.energy.DefaultEnergyHandler;
import com.enginemachining.capabilities.storage.EnergyStorage;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class ModdedCapabilities {
    private ModdedCapabilities() { }

    @CapabilityInject(IEnergyHandler.class)
    public static Capability<IEnergyHandler> ENERGY;

    public static void register() {
        CapabilityManager.INSTANCE.register(IEnergyHandler.class, new EnergyStorage(), DefaultEnergyHandler::new);
    }
}
