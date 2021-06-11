package com.enginemachining.tileentities;

import com.enginemachining.api.energy.IEnergyHandler;
import com.enginemachining.capabilities.ModdedCapabilities;
import com.enginemachining.handlers.energy.EnergyLimiterHandler;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PowerLimiterTile extends TileEntity {
    private final LazyOptional<IEnergyHandler> energy_handler = LazyOptional.of(EnergyLimiterHandler::new);

    public PowerLimiterTile() {
        super(ModdedTileEntities.power_limiter.get());
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
}
