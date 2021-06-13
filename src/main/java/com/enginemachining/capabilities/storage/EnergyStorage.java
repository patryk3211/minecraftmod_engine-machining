package com.enginemachining.capabilities.storage;

import com.enginemachining.api.energy.IEnergyHandler;
import com.enginemachining.capabilities.energy.DefaultEnergyHandler;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class EnergyStorage implements Capability.IStorage<IEnergyHandler> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<IEnergyHandler> capability, IEnergyHandler instance, Direction side) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putFloat("energy", instance.getStoredPower());
        return nbt;
    }

    @Override
    public void readNBT(Capability<IEnergyHandler> capability, IEnergyHandler instance, Direction side, INBT nbt) {
        CompoundNBT e = (CompoundNBT) nbt;
        if(!(instance instanceof DefaultEnergyHandler)) throw new IllegalArgumentException("Cannot deserialize a non default implementation.");
        ((DefaultEnergyHandler)instance).energy = e.getFloat("energy");
    }
}
