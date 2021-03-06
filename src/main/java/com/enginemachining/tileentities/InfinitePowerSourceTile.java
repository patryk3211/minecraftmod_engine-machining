package com.enginemachining.tileentities;

import com.enginemachining.api.energy.IEnergyHandler;
import com.enginemachining.capabilities.ModdedCapabilities;
import com.enginemachining.handlers.IEnergyHandlerProvider;
import com.enginemachining.handlers.energy.InfiniteEnergySource;
import com.enginemachining.utils.EnergyNetwork;
import com.enginemachining.utils.PipeNetwork;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.antlr.v4.runtime.misc.NotNull;

import javax.annotation.Nullable;

public class InfinitePowerSourceTile extends TileEntity implements ITickableTileEntity, IEnergyHandlerProvider {
    private IEnergyHandler handler = new InfiniteEnergySource();
    private LazyOptional<IEnergyHandler> energyHandler = LazyOptional.of(() -> handler);

    private boolean firstTick = true;

    public InfinitePowerSourceTile() {
        super(ModdedTileEntities.infinite_power_source.get());
    }

    @Override
    public IEnergyHandler getEnergyHandler() {
        return handler;
    }

    @Override
    public void tick() {
        if(!level.isClientSide) {
            if(firstTick) {
                if(network == null) PipeNetwork.addTraceable(this, ModdedCapabilities.ENERGY, () -> new EnergyNetwork(level));
                firstTick = false;
            }
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
        if(oldNetwork == network) network = newNetwork;
    }

    @Override
    public boolean canConnect(Direction side, Capability<?> capability) {
        if(capability == ModdedCapabilities.ENERGY) {
            return true;
        }
        return false;
    }

    @Override
    public Type getSideType(Direction side, Capability<?> capability) {
        if(capability == ModdedCapabilities.ENERGY) return Type.SENDER;
        return Type.NONE;
    }

    @Override
    public Type getMainType(Capability<?> capability) {
        if(capability == ModdedCapabilities.ENERGY) return Type.SENDER;
        return Type.NONE;
    }

    @Override
    public float getResistance() {
        return 0;
    }

    @Override
    public BlockPos getBlockPosition() {
        return worldPosition;
    }

    @Override
    public World getWorld() {
        return level;
    }
}
