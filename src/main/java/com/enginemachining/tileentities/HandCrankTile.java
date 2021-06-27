package com.enginemachining.tileentities;

import com.enginemachining.api.rotation.IKineticEnergyHandler;
import com.enginemachining.api.rotation.IRotationalNetwork;
import com.enginemachining.api.rotation.RotationalNetwork;
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
    private IRotationalNetwork network;
    private boolean firstTick = true;

    private final LazyOptional<IKineticEnergyHandler> handler = LazyOptional.of(() -> new IKineticEnergyHandler() {
        @Override
        public IRotationalNetwork getNetwork() {
            return network;
        }

        @Override
        public void setNetwork(IRotationalNetwork network) {
            HandCrankTile.this.network = network;
        }

        @Override
        public float getInertiaMass() {
            return 0.5f;
        }

        @Override
        public float getFriction() {
            return 0.1f;
        }
    });

    public HandCrankTile() {
        super(ModdedTileEntities.handcrank.get());
    }


    @Override
    public void tick() {
        if(!level.isClientSide) {
            if(firstTick) {
                firstTick = false;
                RotationalNetwork.addToNetwork(worldPosition, level);
            }
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == ModdedCapabilities.ROTATION) {
            if(side == getBlockState().getValue(BlockStateProperties.FACING)) return handler.cast();
            else if(side == null) return handler.cast();
            return LazyOptional.empty();
        }
        return super.getCapability(cap, side);
    }

    public IRotationalNetwork getNetwork() {
        return network;
    }
}
