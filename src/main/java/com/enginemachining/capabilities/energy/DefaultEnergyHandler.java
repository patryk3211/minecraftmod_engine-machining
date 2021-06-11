package com.enginemachining.capabilities.energy;

import com.enginemachining.api.energy.IEnergyHandler;
import net.minecraft.util.Direction;

import javax.annotation.Nullable;

public class DefaultEnergyHandler implements IEnergyHandler {
    public int energy;

    @Override
    public boolean canSideExtract(@Nullable Direction dir) {
        return true;
    }

    @Override
    public boolean canSideInsert(@Nullable Direction dir) {
        return true;
    }

    @Override
    public int insertPower(int power, boolean simulate) {
        if(!simulate) energy += power;
        return power;
    }

    @Override
    public int extractPower(int power, boolean simulate) {
        if(!simulate) energy -= power;
        return power;
    }

    @Override
    public int getStoredPower() {
        return energy;
    }

    @Override
    public int getMaxPower() {
        return Integer.MAX_VALUE;
    }
}
