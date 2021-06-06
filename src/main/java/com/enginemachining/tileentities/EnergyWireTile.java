package com.enginemachining.tileentities;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;

public class EnergyWireTile extends TileEntity {
    byte disconnectMask;

    public EnergyWireTile() {
        super(ModdedTileEntities.energy_wire.get());

        disconnectMask = 0;
    }

    @Override
    public void deserializeNBT(BlockState state, CompoundNBT nbt) {
        disconnectMask = nbt.getByte("disconnectSides");
        super.deserializeNBT(state, nbt);
    }



    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compound = super.serializeNBT();
        compound.putByte("disconnectSides", disconnectMask);
        return compound;
    }

    public boolean isSideConnectable(Direction side) {
        return ((disconnectMask >> side.get3DDataValue()) & 1) == 0;
    }

    public void setSideConnectable(Direction side, boolean canConnect) {
        disconnectMask &= ~(1 << side.get3DDataValue());
        disconnectMask |= ((canConnect ? 0 : 1) << side.get3DDataValue());
    }
}
