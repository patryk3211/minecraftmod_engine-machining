package com.enginemachining.handlers.energy;

import com.enginemachining.api.energy.IEnergyHandler;
import net.minecraft.util.Direction;

import javax.annotation.Nullable;

public class MeasuringEnergyHandler implements IEnergyHandler {
    private final IEnergyHandler handler;
    private float insertAmount;
    private float extractAmount;

    public MeasuringEnergyHandler(IEnergyHandler toMeasure) {
        handler = toMeasure;
        this.insertAmount = 0;
        this.extractAmount = 0;
    }

    public void resetCounters() {
        insertAmount = 0;
        extractAmount = 0;
    }

    public float getInsertAmount() {
        return insertAmount;
    }

    public float getExtractAmount() {
        return extractAmount;
    }

    @Override
    public float insertPower(float power, boolean simulate) {
        float amount = handler.insertPower(power, simulate);
        if(!simulate) insertAmount += amount;
        return amount;
    }

    @Override
    public float extractPower(float power, boolean simulate) {
        float amount = handler.extractPower(power, simulate);
        if(!simulate) extractAmount += amount;
        return amount;
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