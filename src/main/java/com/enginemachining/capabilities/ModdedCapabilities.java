package com.enginemachining.capabilities;

import com.enginemachining.api.energy.IEnergyHandler;
import com.enginemachining.api.rotation.IKineticEnergyHandler;
import com.enginemachining.capabilities.energy.DefaultEnergyHandler;
import com.enginemachining.capabilities.rotation.ConstantSpeedKineticEnergyHandler;
import com.enginemachining.capabilities.storage.EnergyStorage;
import com.enginemachining.capabilities.storage.KineticStorage;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class ModdedCapabilities {
    private ModdedCapabilities() { }

    @CapabilityInject(IEnergyHandler.class)
    public static Capability<IEnergyHandler> ENERGY;
    @CapabilityInject(IKineticEnergyHandler.class)
    public static Capability<IKineticEnergyHandler> ROTATION;

    public static void register() {
        CapabilityManager.INSTANCE.register(IEnergyHandler.class, new EnergyStorage(), DefaultEnergyHandler::new);
        CapabilityManager.INSTANCE.register(IKineticEnergyHandler.class, new KineticStorage(), () -> new ConstantSpeedKineticEnergyHandler(1));
    }
}
