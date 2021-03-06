package com.enginemachining.handlers.energy;

import com.enginemachining.api.energy.ISerializableEnergyHandler;
import net.minecraft.nbt.CompoundNBT;

public class EnergyHandler implements ISerializableEnergyHandler {
    int maxEnergy;
    float currentEnergy;

    public EnergyHandler(int maxEnergy) {
        this.maxEnergy = maxEnergy;
        currentEnergy = 0;
    }

    public void setMaxEnergy(int limit) {
        this.maxEnergy = limit;
    }

    public float insertPower(float power, boolean simulate) {
        if(!canReceive()) return 0;

        float leftToFull = maxEnergy - currentEnergy;
        if(leftToFull < power) {
            if(!simulate) currentEnergy += leftToFull;
            return leftToFull;
        }

        if(!simulate) currentEnergy += power;
        return power;
    }

    @Override
    public float extractPower(float power, boolean simulate) {
        if(!canExtract()) return 0;

        if(currentEnergy < power) {
            float ret = currentEnergy;
            if(!simulate) currentEnergy = 0;
            return ret;
        }

        if(!simulate) currentEnergy -= power;
        return power;
    }

    @Override
    public float getStoredPower() {
        return currentEnergy;
    }

    @Override
    public float getMaxPower() {
        return maxEnergy;
    }

    public boolean canExtract() {
        return currentEnergy > 0;
    }

    public boolean canReceive() {
        return maxEnergy > currentEnergy;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putFloat("energyStored", currentEnergy);
        nbt.putInt("capacity", maxEnergy);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        currentEnergy = nbt.getFloat("energyStored");
        maxEnergy = nbt.getInt("capacity");
    }
}
