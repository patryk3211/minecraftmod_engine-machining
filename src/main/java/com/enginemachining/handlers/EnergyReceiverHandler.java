package com.enginemachining.handlers;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyReceiverHandler implements IEnergyStorage, INBTSerializable<CompoundNBT> {
    int maxEnergy;
    int currentEnergy;

    public EnergyReceiverHandler(int maxEnergy) {
        this.maxEnergy = maxEnergy;
        currentEnergy = 0;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if(!canReceive()) return 0;

        int leftToFull = maxEnergy - currentEnergy;
        if(leftToFull < maxReceive) {
            if(!simulate) currentEnergy += leftToFull;
            return leftToFull;
        }

        if(!simulate) currentEnergy += maxReceive;
        return maxReceive;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public int getEnergyStored() {
        return currentEnergy;
    }

    @Override
    public int getMaxEnergyStored() {
        return maxEnergy;
    }

    @Override
    public boolean canExtract() {
        return false;
    }

    @Override
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
}