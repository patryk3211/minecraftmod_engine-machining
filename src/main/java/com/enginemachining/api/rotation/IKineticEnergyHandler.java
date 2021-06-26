package com.enginemachining.api.rotation;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IKineticEnergyHandler {
    /**
     * This method is used to get the rotational speed.
     * @return speed
     */
    float getSpeed();

    /**
     * This method is used to get the current angle of the spinning shaft.
     * It is only required to work on the client side.
     * @return angle of shaft in degrees.
     */
    @OnlyIn(Dist.CLIENT)
    double getCurrentAngle();

    /**
     * This method is used to get the current network of a kinetic device
     * @return The rotational network of this device
     */
    RotationalNetwork getNetwork();

    /**
     * This method is used to set the current network of a kinetic device
     * @param network The new network
     */
    void setNetwork(RotationalNetwork network);
}
