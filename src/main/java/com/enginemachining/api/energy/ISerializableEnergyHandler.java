package com.enginemachining.api.energy;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface ISerializableEnergyHandler extends IEnergyHandler, INBTSerializable<CompoundNBT> {
}
