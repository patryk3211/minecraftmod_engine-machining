package com.enginemachining.tileentities;

import com.enginemachining.api.energy.IEnergyHandler;
import com.enginemachining.blocks.PowerLimiter;
import com.enginemachining.capabilities.ModdedCapabilities;
import com.enginemachining.containers.PowerLimiterContainer;
import com.enginemachining.handlers.IEnergyHandlerProvider;
import com.enginemachining.handlers.energy.ConstrainedEnergyHandler;
import com.enginemachining.handlers.energy.EnergyHandler;
import com.enginemachining.handlers.energy.MeasuringEnergyHandler;
import com.enginemachining.handlers.energy.ToggableEnergyHandler;
import com.enginemachining.utils.EnergyNetwork;
import com.enginemachining.utils.IPipeTraceable;
import com.enginemachining.utils.PipeNetwork;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class PowerLimiterTile extends TileEntity implements IPipeTraceable, ITickableTileEntity, IEnergyHandlerProvider, INamedContainerProvider {
    private final EnergyHandler handler = new EnergyHandler(0);
    private final ConstrainedEnergyHandler constrained = new ConstrainedEnergyHandler(handler);
    private final MeasuringEnergyHandler measurer = new MeasuringEnergyHandler(constrained);
    private final ToggableEnergyHandler toggle = new ToggableEnergyHandler(measurer, false, true, false);
    private final LazyOptional<IEnergyHandler> energy_handler = LazyOptional.of(() -> toggle);

    private boolean firstTick = true;
    private int maxExtractAmount = 0;

    public IIntArray data = new IIntArray() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0: return (int) handler.getStoredPower();
                case 1: return (int) handler.getMaxPower();
                case 2: return (int) measurer.getExtractAmount();
                case 3: return maxExtractAmount;
                case 4: return toggle.isEnabled() ? 1 : 0;
                case 5: return constrained.getMaxRate();
                default: return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 4: toggle.setEnabled(value == 1);
                case 5: constrained.setMaxRate(value);
                default: break;
            }
        }

        @Override
        public int getCount() {
            return 6;
        }
    };

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
        if(!level.isClientSide) {
            if (firstTick) {
                // Read the max power limiter power from it's block
                if(getBlockState().getBlock() instanceof PowerLimiter) {
                    PowerLimiter limiterBlock = (PowerLimiter) getBlockState().getBlock();
                    if(handler.getMaxPower() == 0) {
                        handler.setMaxEnergy(limiterBlock.getMaxPowerLimit());
                        constrained.setMaxRate(limiterBlock.getMaxPowerLimit() / 2);
                    }
                    if(maxExtractAmount == 0) maxExtractAmount = limiterBlock.getMaxPowerLimit();
                }
                if (networks.size() == 0) PipeNetwork.addTraceable(this, ModdedCapabilities.ENERGY, () -> new EnergyNetwork(level));
                firstTick = false;
            }
            measurer.resetCounters();
        }
    }

    @Override
    public IEnergyHandler getEnergyHandler() {
        return toggle;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("Power Limiter");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
        return new PowerLimiterContainer(id, inventory, this);
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        toggle.deserializeNBT(nbt.getCompound("handler"));
        super.load(state, nbt);
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        nbt.put("handler", toggle.serializeNBT());
        return super.save(nbt);
    }
}
