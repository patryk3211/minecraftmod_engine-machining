package com.enginemachining.handlers.energy;

import com.enginemachining.api.energy.IEnergyHandler;
import com.enginemachining.api.energy.ISerializableEnergyHandler;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public class ConstrainedEnergyHandler implements ISerializableEnergyHandler {
    private int maxRate;
    private final ISerializableEnergyHandler parentHandler;

    public ConstrainedEnergyHandler(ISerializableEnergyHandler parentHandler) {
        this.maxRate = 0;
        this.parentHandler = parentHandler;
    }

    public void setMaxRate(int rate) {
        maxRate = rate;
    }

    public float insertPower(float power, boolean simulate) {
        return parentHandler.insertPower(Float.min(power, maxRate), simulate);
    }

    @Override
    public float extractPower(float power, boolean simulate) {
        return parentHandler.extractPower(Float.min(power, maxRate), simulate);
    }

    @Override
    public float getStoredPower() {
        return parentHandler.getStoredPower();
    }

    @Override
    public float getMaxPower() {
        return parentHandler.getMaxPower();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = parentHandler.serializeNBT();
        nbt.putInt("maxRate", maxRate);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        parentHandler.deserializeNBT(nbt);
        maxRate = nbt.getInt("maxRate");
    }
}
