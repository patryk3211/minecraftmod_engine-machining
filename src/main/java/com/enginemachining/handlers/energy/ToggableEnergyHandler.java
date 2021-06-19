package com.enginemachining.handlers.energy;

import com.enginemachining.api.energy.IEnergyHandler;

public class ToggableEnergyHandler implements IEnergyHandler {
    private final IEnergyHandler handler;
    private boolean state;

    public ToggableEnergyHandler(IEnergyHandler handler, boolean enabled) {
        this.handler = handler;
        state = enabled;
    }

    public boolean isEnabled() {
        return state;
    }

    public void setEnabled(boolean isEnabled) {
        state = isEnabled;
    }

    @Override
    public float insertPower(float power, boolean simulate) {
        if(!state) return 0;
        return handler.insertPower(power, simulate);
    }

    @Override
    public float extractPower(float power, boolean simulate) {
        if(!state) return 0;
        return handler.extractPower(power, simulate);
    }

    @Override
    public float getStoredPower() {
        return handler.getStoredPower();
    }

    @Override
    public float getMaxPower() {
        return handler.getMaxPower();
    }
}
