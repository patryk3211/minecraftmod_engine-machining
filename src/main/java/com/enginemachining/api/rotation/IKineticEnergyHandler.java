package com.enginemachining.api.rotation;

public interface IKineticEnergyHandler {
    /**
     * This method is used to get the current network of a kinetic device
     * @return The rotational network of this device
     */
    IRotationalNetwork getNetwork();

    /**
     * This method is used to set the current network of a kinetic device
     * @param network The new network
     */
    void setNetwork(IRotationalNetwork network);

    /**
     * This method is used to get mass of the rotating part of this block.
     * This mass is used to calculate the inertia of a network.
     * @return Mass
     */
    float getInertiaMass();

    /**
     * This method is used to get friction of the rotating part of this block.
     * This friction will be used to calculate speed loss per tick.
     * @return Friction
     */
    float getFriction();
}
