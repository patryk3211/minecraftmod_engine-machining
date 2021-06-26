package com.enginemachining.capabilities.rotation;

import com.enginemachining.api.rotation.IKineticEnergyHandler;
import com.enginemachining.api.rotation.RotationalNetwork;

public class ConstantSpeedKineticEnergyHandler implements IKineticEnergyHandler {
    private final float rotationalSpeed;
    private RotationalNetwork network;

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

    @Override
    public RotationalNetwork getNetwork() {
        return network;
    }

    @Override
    public void setNetwork(RotationalNetwork network) {
        this.network = network;
    }
}
