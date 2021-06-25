package com.enginemachining.tileentities;

import com.enginemachining.api.energy.IEnergyHandler;
import com.enginemachining.api.rotation.IKineticEnergyHandler;
import com.enginemachining.capabilities.ModdedCapabilities;
import com.enginemachining.handlers.IEnergyHandlerProvider;
import com.enginemachining.handlers.energy.EnergyHandler;
import com.enginemachining.utils.PipeNetwork;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class KineticGeneratorTile extends TileEntity implements ITickableTileEntity, IEnergyHandlerProvider {
    private float currentGeneration = 0;
    private float storedPower = 0;
    // TODO: [25.06.2021] To future me, please use LazyOptionals in the EnergyNetwork
    private IEnergyHandler ehandler = new IEnergyHandler() {
        @Override
        public float insertPower(float power, boolean simulate) {
            // Cannot insert into a generator.
            return 0;
        }

        @Override
        public float extractPower(float power, boolean simulate) {
            if(storedPower <= 0) return 0;

            if(storedPower < power) {
                float ret = storedPower;
                if(!simulate) storedPower = 0;
                return ret;
            }

            if(!simulate) storedPower -= power;
            return power;
        }

        @Override
        public float getStoredPower() {
            return storedPower;
        }

        @Override
        public float getMaxPower() {
            return currentGeneration;
        }
    };
    private final LazyOptional<IEnergyHandler> handler = LazyOptional.of(() -> ehandler);

    public KineticGeneratorTile() {
        super(ModdedTileEntities.kinetic_generator.get());
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == ModdedCapabilities.ENERGY) {
            if(side == getBlockState().getValue(BlockStateProperties.FACING).getOpposite()) return handler.cast();
            return LazyOptional.empty();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void tick() {
        if(!level.isClientSide) {
            Direction facing = getBlockState().getValue(BlockStateProperties.FACING);
            TileEntity te = level.getBlockEntity(worldPosition.offset(facing.getNormal()));
            if(te != null) {
                LazyOptional<IKineticEnergyHandler> capability = te.getCapability(ModdedCapabilities.ROTATION, facing.getOpposite());
                capability.ifPresent(handler -> {
                    float generation = handler.getSpeed();
                    currentGeneration = generation * 50f;
                    storedPower = generation * 50f;
                });
            }
        }
    }

    @Override
    public IEnergyHandler getEnergyHandler() {
        return ehandler;
    }


    private PipeNetwork network;
    @Override
    public PipeNetwork getNetwork(@Nullable Direction side) {
        return network;
    }

    @Override
    public void setNetwork(@Nullable Direction side, PipeNetwork network) {
        this.network = network;
    }

    @Override
    public void removeNetwork(PipeNetwork network) {
        if(this.network == network) this.network = null;
    }

    @Override
    public void replaceNetwork(PipeNetwork oldNetwork, PipeNetwork newNetwork) {
        if(this.network == oldNetwork) this.network = newNetwork;
    }

    @Override
    public boolean canConnect(Direction side, Capability<?> capability) {
        if(capability == ModdedCapabilities.ENERGY) {
            if(side == getBlockState().getValue(BlockStateProperties.FACING).getOpposite()) return true;
        }
        return false;
    }

    @Override
    public Type getSideType(Direction side, Capability<?> capability) {
        if(capability == ModdedCapabilities.ENERGY) {
            if(side == getBlockState().getValue(BlockStateProperties.FACING).getOpposite()) return Type.SENDER;
        }
        return Type.NONE;
    }

    @Override
    public Type getMainType(Capability<?> capability) {
        if(capability == ModdedCapabilities.ENERGY) return Type.SENDER;
        return Type.NONE;
    }

    @Override
    public BlockPos getBlockPosition() {
        return worldPosition;
    }

    @Override
    public World getWorld() {
        return level;
    }

    @Override
    public float getResistance() {
        return 0;
    }
}
