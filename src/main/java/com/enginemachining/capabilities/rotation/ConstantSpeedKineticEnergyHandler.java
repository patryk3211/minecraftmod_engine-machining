package com.enginemachining.capabilities.rotation;

import com.enginemachining.api.rotation.IKineticEnergyHandler;

public class ConstantSpeedKineticEnergyHandler implements IKineticEnergyHandler {
    private final float rotationalSpeed;

    public ConstantSpeedKineticEnergyHandler(float rotationalSpeed) {
        this.rotationalSpeed = rotationalSpeed;
    }

    @Override
    public float getSpeed() {
        return rotationalSpeed;
    }

    @Override
    public double getCurrentAngle() {
        return 0;
    }
}
