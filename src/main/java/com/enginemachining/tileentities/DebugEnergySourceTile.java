package com.enginemachining.tileentities;

import com.enginemachining.capabilities.ModdedCapabilities;
import com.enginemachining.handlers.IEnergySender;
import com.enginemachining.handlers.energy.InfiniteEnergySource;
import com.enginemachining.utils.PipeNetwork;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
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

        }
    }

    @Override
    public TileEntity getTileEntity() {
        return this;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ModdedCapabilities.ENERGY) return energyHandler.cast();
        return super.getCapability(cap, side);
    }

    private PipeNetwork network;

    @Override
    public PipeNetwork getNetwork() {
        return network;
    }

    @Override
    public void setNetwork(PipeNetwork network) {
        this.network = network;
    }

    @Override
    public boolean canConnect(Direction side, Capability<?> capability) {
        if(capability == ModdedCapabilities.ENERGY) {
            return true;
        }
        return false;
    }

    @Override
    public Type getSideType(Direction side) {
        return Type.SENDER;
    }

    @Override
    public BlockPos getPosition() {
        return worldPosition;
    }

    @Override
    public float getResistance() {
        return 0;
    }
}
