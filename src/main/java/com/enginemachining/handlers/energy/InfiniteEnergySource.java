package com.enginemachining.handlers.energy;

import com.enginemachining.api.energy.IEnergyHandler;

public class InfiniteEnergySource implements IEnergyHandler {

    public InfiniteEnergySource() { }

    @Override
    public float insertPower(float power, boolean simulate) { return 0; }

    @Override
    public float extractPower(float power, boolean simulate) { return Float.min(power, 100); }

    @Override
    public float getStoredPower() {
        return Integer.MAX_VALUE;
    }

    @Override
    public float getMaxPower() {
        return Integer.MAX_VALUE;
    }
}
