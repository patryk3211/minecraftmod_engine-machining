package com.enginemachining.api.rotation;

import net.minecraft.state.EnumProperty;
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
}
