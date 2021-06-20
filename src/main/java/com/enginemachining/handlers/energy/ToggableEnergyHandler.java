package com.enginemachining.handlers.energy;

import com.enginemachining.api.energy.IEnergyHandler;
import com.enginemachining.api.energy.ISerializableEnergyHandler;
import net.minecraft.nbt.CompoundNBT;

public class ToggableEnergyHandler implements ISerializableEnergyHandler {
    private final ISerializableEnergyHandler handler;
    private boolean state;
    private boolean extractAffected;
    private boolean insertAffected;

    public ToggableEnergyHandler(ISerializableEnergyHandler handler, boolean enabled, boolean extractAffected, boolean insertAffected) {
        this.handler = handler;
        state = enabled;
        this.extractAffected = extractAffected;
        this.insertAffected = insertAffected;
    }

    public boolean isEnabled() {
        return state;
    }

    public void setEnabled(boolean isEnabled) {
        state = isEnabled;
    }

    @Override
    public float insertPower(float power, boolean simulate) {
        if(!state && insertAffected) return 0;
        return handler.insertPower(power, simulate);
    }

    @Override
    public float extractPower(float power, boolean simulate) {
        if(!state && extractAffected) return 0;
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

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = handler.serializeNBT();
        nbt.putBoolean("state", state);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        handler.deserializeNBT(nbt);
        state = nbt.getBoolean("state");
    }
}
