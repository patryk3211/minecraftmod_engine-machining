package com.enginemachining.tileentities;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public class EnergyWireTile extends TileEntity {
    public byte disconnectMask;

    public EnergyWireTile() {
        super(ModdedTileEntities.energy_wire.get());

        disconnectMask = 0;
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        byte disconnectMask = nbt.getByte("disconnectSides");
        if(!level.isClientSide && disconnectMask != this.disconnectMask) {
            this.disconnectMask = disconnectMask;
            level.blockUpdated(getBlockPos(), getBlockState().getBlock());
        }
        super.load(state, nbt);
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        nbt.putByte("disconnectSides", disconnectMask);
        return super.save(nbt);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putByte("disconnectSides", disconnectMask);
        return new SUpdateTileEntityPacket(worldPosition, 1, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        disconnectMask = pkt.getTag().getByte("disconnectSides");
    }

    public boolean isSideConnectable(Direction side) {
        return ((disconnectMask >> side.get3DDataValue()) & 1) == 0;
    }

    public void setSideConnectable(Direction side, boolean canConnect) {
        disconnectMask &= ~(1 << side.get3DDataValue());
        disconnectMask |= ((canConnect ? 0 : 1) << side.get3DDataValue());
    }
}
