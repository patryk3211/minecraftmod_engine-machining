package com.enginemachining.tileentities;

import com.enginemachining.utils.IPipeReceiver;
import com.enginemachining.utils.IPipeSender;
import com.enginemachining.utils.IPipeTraceable;
import com.enginemachining.utils.PipeNetwork;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;

public class EnergyWireTile extends TileEntity implements IPipeTraceable, ITickableTileEntity {
    private byte disconnectMask;
    private boolean firstTick;

    public EnergyWireTile() {
        super(ModdedTileEntities.energy_wire.get());

        disconnectMask = 0;
        firstTick = true;
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        this.disconnectMask = nbt.getByte("disconnectSides");
        if(level != null && !level.isClientSide) {
            level.blockUpdated(getBlockPos(), getBlockState().getBlock());
        }
        super.load(state, nbt);
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        nbt.putByte("disconnectSides", disconnectMask);
        return super.save(nbt);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putByte("disconnectSides", disconnectMask);
        return new SUpdateTileEntityPacket(worldPosition, 1, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        disconnectMask = pkt.getTag().getByte("disconnectSides");
    }

    public boolean isSideConnectable(Direction side) {
        return ((disconnectMask >> side.get3DDataValue()) & 1) == 0;
    }

    public void setSideConnectable(Direction side, boolean canConnect) {
        disconnectMask &= ~(1 << side.get3DDataValue());
        disconnectMask |= ((canConnect ? 0 : 1) << side.get3DDataValue());
    }

    private PipeNetwork network;
    @Override
    public PipeNetwork getNetwork() { return network; }
    @Override
    public void setNetwork(PipeNetwork network) { this.network = network; }

    @Override
    public boolean canConnect(Direction side, Capability<?> capability) {
        if(capability == CapabilityEnergy.ENERGY) return isSideConnectable(side);
        return false;
    }

    @Override
    public void tick() {
        if(firstTick) {
            if(!level.isClientSide) {
                if(network == null) {
                    network = new PipeNetwork(level, EnergyWireTile.class, IPipeReceiver.class, IPipeSender.class, CapabilityEnergy.ENERGY);
                    network.traceNetwork(getBlockPos());
                    network.dump();
                }
            }
            firstTick = false;
        }
    }
}
