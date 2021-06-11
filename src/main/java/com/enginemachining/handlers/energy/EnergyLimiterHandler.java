package com.enginemachining.handlers.energy;

import com.enginemachining.api.energy.IEnergyHandler;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public class EnergyLimiterHandler implements IEnergyHandler, INBTSerializable<CompoundNBT> {
    int maxEnergy;
    int currentEnergy;

    public EnergyLimiterHandler() {
        this.maxEnergy = 0;
        currentEnergy = 0;
    }

    public void setNewLimit(int limit) {
        maxEnergy = limit;
        if(currentEnergy > maxEnergy) currentEnergy = maxEnergy;
    }

    public int insertPower(int power, boolean simulate) {
        if(!canReceive()) return 0;

        int leftToFull = maxEnergy - currentEnergy;
        if(leftToFull < power) {
            if(!simulate) currentEnergy += leftToFull;
            return leftToFull;
        }

        if(!simulate) currentEnergy += power;
        return power;
    }

    @Override
    public int extractPower(int power, boolean simulate) {
        if(!canExtract()) return 0;

        if(currentEnergy < power) {
            int ret = currentEnergy;
            if(!simulate) currentEnergy = 0;
            return ret;
        }

        if(!simulate) currentEnergy -= power;
        return power;
    }

    @Override
    public int getStoredPower() {
        return currentEnergy;
    }

    @Override
    public int getMaxPower() {
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
        nbt.putInt("energyStored", currentEnergy);
        nbt.putInt("capacity", maxEnergy);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        currentEnergy = nbt.getInt("energyStored");
        maxEnergy = nbt.getInt("capacity");
    }

    @Override
    public boolean canSideExtract(@Nullable Direction dir) {
        return true;
    }

    @Override
    public boolean canSideInsert(@Nullable Direction dir) {
        return true;
    }
}
