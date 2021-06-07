package com.enginemachining.tileentities;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
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
    public void load(BlockState state, CompoundNBT nbt) {
        disconnectMask = nbt.getByte("disconnectSides");
        super.load(state, nbt);
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        nbt.putByte("disconnectSides", disconnectMask);
        return super.save(nbt);
    }

    public boolean isSideConnectable(Direction side) {
        return ((disconnectMask >> side.get3DDataValue()) & 1) == 0;
    }

    public void setSideConnectable(Direction side, boolean canConnect) {
        disconnectMask &= ~(1 << side.get3DDataValue());
        disconnectMask |= ((canConnect ? 0 : 1) << side.get3DDataValue());
    }
}
