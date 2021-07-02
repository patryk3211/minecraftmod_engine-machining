package com.enginemachining.tileentities;

import com.enginemachining.api.rotation.IKineticEnergyHandler;
import com.enginemachining.api.rotation.IRotationalNetwork;
import com.enginemachining.api.rotation.RotationalNetwork;
import com.enginemachining.capabilities.ModdedCapabilities;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ShaftTile extends TileEntity implements ITickableTileEntity {
    private boolean firstTick = true;

    private IRotationalNetwork network;

    private final LazyOptional<IKineticEnergyHandler> handler = LazyOptional.of(() -> new IKineticEnergyHandler() {
        @Override
        public IRotationalNetwork getNetwork() {
            return network;
        }

        @Override
        public void setNetwork(IRotationalNetwork network) {
            ShaftTile.this.network = network;
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

    public ShaftTile() {
        super(ModdedTileEntities.shaft.get());
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
            Direction.Axis axis = getBlockState().getValue(BlockStateProperties.AXIS);
            if(side == null || side.getAxis() == axis) return handler.cast();
            return LazyOptional.empty();
        }
        return super.getCapability(cap, side);
    }

    public IRotationalNetwork getNetwork() {
        return network;
    }
}
