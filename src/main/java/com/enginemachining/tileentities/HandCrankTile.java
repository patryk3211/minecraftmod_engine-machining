package com.enginemachining.tileentities;

import com.enginemachining.api.rotation.IKineticEnergyHandler;
import com.enginemachining.capabilities.ModdedCapabilities;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class HandCrankTile extends TileEntity implements ITickableTileEntity {
    public float velocity;

    public double angle;
    public double toRotate;

    private LazyOptional<IKineticEnergyHandler> handler = LazyOptional.of(() -> new IKineticEnergyHandler() {
        @Override
        public float getSpeed() {
            return velocity;
        }

        @Override
        public double getCurrentAngle() {
            return angle;
        }
    });

    public HandCrankTile() {
        super(ModdedTileEntities.handcrank.get());

        velocity = 0;
        angle = 0;
        toRotate = 0;
    }


    @Override
    public void tick() {
        if(level.isClientSide) {
            toRotate = (velocity * 360f) * (1/20f);
        } else {
            if(velocity > 0) {
                velocity -= 0.1f;
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
            }
        }
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putFloat("velocity", velocity);
        return new SUpdateTileEntityPacket(worldPosition, 1, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        velocity = pkt.getTag().getFloat("velocity");
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        velocity = nbt.getFloat("velocity");
        super.load(state, nbt);
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        nbt.putFloat("velocity", velocity);
        return super.save(nbt);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == ModdedCapabilities.ROTATION) {
            if(side == getBlockState().getValue(BlockStateProperties.FACING)) return handler.cast();
            return LazyOptional.empty();
        }
        return super.getCapability(cap, side);
    }
}
