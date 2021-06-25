package com.enginemachining.capabilities.storage;

import com.enginemachining.api.rotation.IKineticEnergyHandler;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class KineticStorage implements Capability.IStorage<IKineticEnergyHandler> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<IKineticEnergyHandler> capability, IKineticEnergyHandler instance, Direction side) {
        return new CompoundNBT(); // Return an empty tag, since the default constant speed implementation's speed cannot be modified.
    }

    @Override
    public void readNBT(Capability<IKineticEnergyHandler> capability, IKineticEnergyHandler instance, Direction side, INBT nbt) {
        // Do nothing.
    }
}
