package com.enginemachining.tileentities;

import com.enginemachining.api.energy.IEnergyHandler;
import com.enginemachining.capabilities.ModdedCapabilities;
import com.enginemachining.handlers.IEnergyHandlerProvider;
import com.enginemachining.handlers.energy.EnergyLimiterHandler;
import com.enginemachining.utils.EnergyNetwork;
import com.enginemachining.utils.IPipeTraceable;
import com.enginemachining.utils.PipeNetwork;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class PowerLimiterTile extends TileEntity implements IPipeTraceable, ITickableTileEntity, IEnergyHandlerProvider {
    private final EnergyLimiterHandler handler = new EnergyLimiterHandler();
    private final LazyOptional<IEnergyHandler> energy_handler = LazyOptional.of(() -> handler);

    private boolean firstTick = true;

    public PowerLimiterTile() {
        super(ModdedTileEntities.power_limiter.get());
        handler.setNewLimit(10);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == ModdedCapabilities.ENERGY) {
            Direction facing = getBlockState().getValue(BlockStateProperties.FACING);
            if(side != facing.getOpposite() && side != facing) return LazyOptional.empty();
            return energy_handler.cast();
        }
        return super.getCapability(cap, side);
    }

    private final Map<Direction, PipeNetwork> networks = new HashMap<>();
    @Override
    public PipeNetwork getNetwork(@Nullable Direction side) {
        return networks.get(side);
    }

    @Override
    public void setNetwork(@Nullable Direction side, PipeNetwork network) {
        // Not getting set properly when traced the first time
        if(side == null) {
            networks.clear();
            for(Direction d : Direction.values()) networks.put(d, network);
        } else {
            networks.remove(side);
            if (network != null) networks.put(side, network);
        }
    }

    @Override
    public boolean canConnect(Direction side, Capability<?> capability) {
        if(capability == ModdedCapabilities.ENERGY) {
            return getCapability(ModdedCapabilities.ENERGY, side).isPresent();
        }
        return false;
    }

    @Override
    public Type getSideType(Direction side, Capability<?> capability) {
        if(capability == ModdedCapabilities.ENERGY) {
            Direction facing = getBlockState().getValue(BlockStateProperties.FACING);
            if(side == facing) return Type.RECEIVER;
            else if(side == facing.getOpposite()) return Type.SENDER;
        }
        return Type.NONE;
    }

    @Override
    public Type getMainType(Capability<?> capability) {
        if(capability == ModdedCapabilities.ENERGY) return Type.TRANSCEIVER;
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

    @Override
    public void tick() {
        if(firstTick) {
            // TODO: Power limiter does not get networks on each of its sides
            if(!level.isClientSide) if(networks.size() == 0) PipeNetwork.addTraceable(this, ModdedCapabilities.ENERGY, () -> new EnergyNetwork(level));
            firstTick = false;
        }
    }

    @Override
    public IEnergyHandler getEnergyHandler() {
        return handler;
    }
}
