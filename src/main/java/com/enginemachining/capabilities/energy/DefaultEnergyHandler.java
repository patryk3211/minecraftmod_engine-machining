package com.enginemachining.capabilities.energy;

import com.enginemachining.api.energy.IEnergyHandler;
import net.minecraft.util.Direction;

import javax.annotation.Nullable;

public class DefaultEnergyHandler implements IEnergyHandler {
    public float energy;

    @Override
    public float insertPower(float power, boolean simulate) {
        if(!simulate) energy += power;
        return power;
    }

    @Override
    public float extractPower(float power, boolean simulate) {
        if(!simulate) energy -= power;
        return power;
    }

    @Override
    public float getStoredPower() {
        return energy;
    }

    @Override
    public float getMaxPower() {
        return Integer.MAX_VALUE;
    }
}
