package com.enginemachining.tileentities;

import com.enginemachining.handlers.EnergyHandler;
import com.enginemachining.handlers.EnergyLimiterHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PowerLimiterTile extends TileEntity {
    private final LazyOptional<IEnergyStorage> energy_handler = LazyOptional.of(EnergyLimiterHandler::new);

    public PowerLimiterTile() {
        super(ModdedTileEntities.power_limiter.get());
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityEnergy.ENERGY) {
            if(side != Direction.NORTH && side != Direction.SOUTH) return LazyOptional.empty();
            return energy_handler.cast();
        }
        return super.getCapability(cap, side);
    }
}
