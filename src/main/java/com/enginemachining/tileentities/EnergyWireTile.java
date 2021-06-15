package com.enginemachining.tileentities;

import com.enginemachining.capabilities.ModdedCapabilities;
import com.enginemachining.handlers.IEnergyReceiver;
import com.enginemachining.handlers.IEnergySender;
import com.enginemachining.utils.EnergyNetwork;
import com.enginemachining.utils.IPipeTraceable;
import com.enginemachining.utils.PipeNetwork;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class EnergyWireTile extends TileEntity implements IPipeTraceable, ITickableTileEntity {
    private byte disconnectMask;
    private boolean firstTick;
    private float powerFlow;

    public EnergyWireTile() {
        super(ModdedTileEntities.energy_wire.get());

        disconnectMask = 0;
        firstTick = true;
        powerFlow = 0;
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
        if(capability == ModdedCapabilities.ENERGY) return isSideConnectable(side);
        return false;
    }

    @Override
    public Type getSideType(Direction side, Capability<?> capability) {
        if(capability == ModdedCapabilities.ENERGY) return Type.PIPE;
        return Type.NONE;
    }

    @Override
    public Type getMainType(Capability<?> capability) {
        if(capability == ModdedCapabilities.ENERGY) return Type.PIPE;
        return Type.NONE;
    }

    @Override
    public float getResistance() {
        return 0;
    }

    public void addPowerFlow(float power) {
        powerFlow += power;
    }
    public float getPowerFlow() {
        return powerFlow;
    }

    @Override
    public void tick() {
        if(firstTick) {
            if(!level.isClientSide) PipeNetwork.addTraceable(this, ModdedCapabilities.ENERGY, () -> new EnergyNetwork(level));
            firstTick = false;
        }
        powerFlow = 0;
    }
}
