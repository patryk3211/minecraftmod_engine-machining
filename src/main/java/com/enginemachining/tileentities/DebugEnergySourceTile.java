package com.enginemachining.tileentities;

import com.enginemachining.handlers.IEnergySender;
import com.enginemachining.handlers.InfiniteEnergySource;
import com.enginemachining.utils.PipeTracer;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import org.antlr.v4.runtime.misc.NotNull;

import javax.annotation.Nullable;

public class DebugEnergySourceTile extends TileEntity implements ITickableTileEntity, IEnergySender {
    private LazyOptional<IEnergyStorage> energyHandler = LazyOptional.of(InfiniteEnergySource::new);

    public DebugEnergySourceTile() {
        super(ModdedTileEntities.debug_energy_source.get());
    }

    @Override
    public IEnergyStorage getHandler() {
        return null;
    }

    @Override
    public void tick() {
        if(!level.isClientSide) {
            PipeTracer.TraceForReceivers(this);
        }
    }

    @Override
    public TileEntity getTileEntity() {
        return this;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityEnergy.ENERGY) return energyHandler.cast();
        return super.getCapability(cap, side);
    }
}
