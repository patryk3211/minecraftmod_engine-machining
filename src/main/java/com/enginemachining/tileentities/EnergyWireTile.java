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
    public void read(BlockState state, CompoundNBT nbt) {
        disconnectMask = nbt.getByte("disconnectSides");
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putByte("disconnectSides", disconnectMask);
        return super.write(compound);
    }

    boolean isSideConnectable(Direction side) {
        return ((disconnectMask >> side.getIndex()) & 1) == 0;
    }

    void setSideConnectable(Direction side, boolean canConnect) {
        disconnectMask &= ~(1 << side.getIndex());
        disconnectMask |= ((canConnect ? 0 : 1) << side.getIndex());
    }
}
