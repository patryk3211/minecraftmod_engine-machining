package com.enginemachining.capabilities.rotation;

import com.enginemachining.api.rotation.IKineticEnergyHandler;
import com.enginemachining.api.rotation.IRotationalNetwork;
import com.enginemachining.api.rotation.RotationalNetwork;

public class ConstantSpeedKineticEnergyHandler implements IKineticEnergyHandler {
    private IRotationalNetwork network;

    public ConstantSpeedKineticEnergyHandler(float rotationalSpeed) { }

    @Override
    public IRotationalNetwork getNetwork() {
        return network;
    }

    @Override
    public void setNetwork(IRotationalNetwork network) {
        this.network = network;
    }

    @Override
    public float getInertiaMass() {
        return 1;
    }

    @Override
    public float getFriction() {
        return 0;
    }
}
