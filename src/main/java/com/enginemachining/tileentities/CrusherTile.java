package com.enginemachining.tileentities;

import com.enginemachining.api.energy.IEnergyHandler;
import com.enginemachining.capabilities.ModdedCapabilities;
import com.enginemachining.containers.CrusherContainer;
import com.enginemachining.handlers.IEnergyHandlerProvider;
import com.enginemachining.handlers.energy.EnergyHandler;
import com.enginemachining.items.ModdedItems;
import com.enginemachining.recipes.CrusherRecipe;
import com.enginemachining.recipes.ModdedRecipeTypes;
import com.enginemachining.utils.EnergyNetwork;
import com.enginemachining.utils.PipeNetwork;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.antlr.v4.runtime.misc.NotNull;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class CrusherTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider, IEnergyHandlerProvider {
    private CrusherContainer container;

    private NonNullList<ItemStack> slots = NonNullList.withSize(3, ItemStack.EMPTY);
    public ISidedInventory blockInventory;
    private LazyOptional<? extends IItemHandler>[] itemHandlers;
    private final EnergyHandler handler = new EnergyHandler(10000);
    private final LazyOptional<IEnergyHandler> energyHandler = LazyOptional.of(() -> handler);

    private boolean enabled;
    private boolean readyToRun;

    private int burnTime;
    private int maxBurnTime;

    private int power;
    public static final int HEAT_MAX = 1000;
    static final int ENERGY_PER_POWER = 1;
    static final int COOLDOWN_PER_TICK = 10;
    static final int MAX_POWER_CHANGE = 100;
    static final int POWER_PER_BURNTICK = 50;

    private boolean firstTick = true;

    public IIntArray trackedData = new IIntArray() {
        @Override
        public int get(int index) {
            AtomicInteger ret;
            switch (index) {
                case 0:
                    return (int)handler.getStoredPower();
                case 1:
                    return (int)handler.getMaxPower();
                case 2:
                    return enabled ? 1 : 0;
                case 3:
                    return power;
                case 4:
                    return burnTime;
                case 5:
                    return maxBurnTime;
                default:
                    return -1;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 2:
                    enabled = value != 0;
                    break;
                default:
                    break;
            }
        }

        @Override
        public int getCount() {
            return 6;
        }
    };

    public CrusherTile() {
        super(ModdedTileEntities.crusher.get());
        blockInventory = new ISidedInventory() {
            @Override
            public int[] getSlotsForFace(Direction side) {
                return new int[] { 0, 1, 2 };
            }

            @Override
            public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, @Nullable Direction direction) {
                return slots.get(index).isEmpty() || (slots.get(index).getCount() < 64 && slots.get(index).getItem() == itemStackIn.getItem());
            }

            @Override
            public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
                return !slots.get(index).isEmpty() && slots.get(index).getCount() >= stack.getCount() && stack.getItem() == slots.get(index).getItem();
            }

            @Override
            public int getContainerSize() {
                return slots.size();
            }

            @Override
            public boolean isEmpty() {
                return slots.get(0).isEmpty() && slots.get(1).isEmpty() && slots.get(2).isEmpty();
            }

            @Override
            public ItemStack getItem(int index) {
                if(index >= getContainerSize()) return null;
                return slots.get(index);
            }

            @Override
            public ItemStack removeItem(int index, int count) {
                if(slots.get(index).isEmpty()) return ItemStack.EMPTY;
                if(slots.get(index).getCount() <= count) {
                    ItemStack ret = slots.get(index).copy();
                    slots.set(index, ItemStack.EMPTY);
                    return ret;
                }
                slots.get(index).setCount(slots.get(index).getCount() - count);
                return new ItemStack(slots.get(index).getItem(), count);
            }

            @Override
            public ItemStack removeItemNoUpdate(int index) {
                ItemStack ret = slots.get(index);
                slots.set(index, ItemStack.EMPTY);
                return ret;
            }

            @Override
            public void setItem(int index, ItemStack stack) {
                slots.set(index, stack);
            }

            @Override
            public void setChanged() {
                CrusherTile.this.setChanged();
            }

            @Override
            public boolean stillValid(PlayerEntity player) {
                return true;
            }

            @Override
            public void clearContent() {
                slots.set(0, ItemStack.EMPTY);
                slots.set(1, ItemStack.EMPTY);
                slots.set(2, ItemStack.EMPTY);
            }
        };
        itemHandlers = SidedInvWrapper.create(blockInventory, Direction.NORTH);
        enabled = false;
        power = 0;
        burnTime = 0;
        maxBurnTime = 0;
        prevRecipe = null;
    }

    private CrusherRecipe prevRecipe;

    @Override
    public void tick() {
        if (!level.isClientSide) {
            if(firstTick) {
                if(network == null) PipeNetwork.addTraceable(this, ModdedCapabilities.ENERGY, () -> new EnergyNetwork(level));
                firstTick = false;
            }

            AtomicBoolean markdirty = new AtomicBoolean(false);
            if(power > 0) {
                power -= COOLDOWN_PER_TICK;
            }
            Item bat = slots.get(2).getItem();
            if(bat == ModdedItems.battery_disposable.get()) {
                if(slots.get(2).getTag() == null || slots.get(2).getTag().getCompound("energy") == null) return;
                CompoundNBT energyTag = slots.get(2).getTag().getCompound("energy");
                int energyLeft = energyTag.getInt("charge");
                int maxTransmit = energyTag.getInt("maxDischargeSpeed");

                float received = 0;
                if(energyLeft < maxTransmit) received = handler.insertPower(energyLeft, false);
                else received = handler.insertPower(maxTransmit, false);

                if(received > 0) {
                    slots.get(2).getTag().getCompound("energy").putInt("charge", (int)(energyLeft - received));
                    markdirty.set(true);
                }
            }
            if(enabled) {
                //The crusher is enabled so it should draw power to heat up.
                int powerToMax = HEAT_MAX - power;
                int wantedChange = Math.min(powerToMax, MAX_POWER_CHANGE);
                float energyUsage = handler.extractPower(wantedChange * ENERGY_PER_POWER, true);
                float availableChange = energyUsage / ENERGY_PER_POWER;
                handler.extractPower(availableChange * ENERGY_PER_POWER, false);
                power += availableChange;
                if(HEAT_MAX - power == 0) readyToRun = true;
                if(availableChange > 0) markdirty.set(true);
            }
            if(HEAT_MAX - power > HEAT_MAX * 0.10f) readyToRun = false;

            if(readyToRun) {
                CrusherRecipe rec = GetRecipe(slots.get(0));
                //If recipe is valid and slot is able to accept the result then proceed with the recipe.
                if(rec != null && (slots.get(1).isEmpty() || (slots.get(1).getItem() == rec.getResultItem().getItem() && slots.get(1).getCount() < slots.get(1).getMaxStackSize()))) {
                    if(prevRecipe != rec) {
                        burnTime = 0;
                        prevRecipe = rec;
                    }
                    maxBurnTime = rec.getTime();
                    if(burnTime < maxBurnTime) {
                        power -= POWER_PER_BURNTICK;
                        burnTime++;
                    }
                    else {
                        //Time completed. Take 1 item out, and spawn another.
                        ItemStack inputStack = slots.get(0);
                        if(inputStack.getCount() > 1) {
                            inputStack.setCount(inputStack.getCount() - 1);
                        } else {
                            slots.set(0, ItemStack.EMPTY);
                        }
                        ItemStack outputStack = slots.get(1);
                        if(outputStack.isEmpty()) {
                            slots.set(1, rec.assemble(blockInventory));
                        } else {
                            outputStack.setCount(outputStack.getCount() + rec.getResultItem().getCount());
                        }
                        burnTime = 0;
                        markdirty.set(true);
                    }
                } else {
                    if(burnTime > 0) burnTime--;
                }
            } else {
                if(burnTime > 0) burnTime--;
            }

            if(markdirty.get()) setChanged();
        }
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("Crusher");
    }

    @Nullable
    @Override
    public Container createMenu(int windowId, PlayerInventory inv, PlayerEntity player) {
        return new CrusherContainer(windowId, inv, this);
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        ItemStackHelper.loadAllItems(nbt, slots);
        //energyHandler.ifPresent((handler) -> {
        handler.deserializeNBT(nbt.getCompound("energy"));
        //});
        enabled = nbt.getBoolean("enabled");
        readyToRun = nbt.getBoolean("ready");
        power = Math.min(nbt.getInt("power"), HEAT_MAX);
        burnTime = nbt.getInt("burnTime");
        super.load(state, nbt);
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        ItemStackHelper.saveAllItems(nbt, slots, true);
        CompoundNBT compound = handler.serializeNBT();
        nbt.put("energy", compound);
        nbt.putBoolean("enabled", enabled);
        nbt.putBoolean("ready", readyToRun);
        nbt.putInt("power", power);
        nbt.putInt("burnTime", burnTime);
        return super.save(nbt);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return itemHandlers[0].cast();
        }
        if(cap == ModdedCapabilities.ENERGY) {
            if(side != getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING))
                return energyHandler.cast();
            else return LazyOptional.empty();
        }
        return super.getCapability(cap, side);
    }

    @Nullable
    private CrusherRecipe GetRecipe(ItemStack input) {
        if(input == null) {
            return null;
        }

        Set<IRecipe<?>> recipes = findRecipeByType(ModdedRecipeTypes.crushing, this.level);
        for(IRecipe<?> recipe : recipes) {
            CrusherRecipe cr = (CrusherRecipe)recipe;
            for(Ingredient in : cr.getIngredients()) {
                if(in.test(input)) return cr;
            }
        }

        return null;
    }

    public static Set<IRecipe<?>> findRecipeByType(IRecipeType<?> type, World world) {
        return world != null ? world.getRecipeManager().getRecipes().stream().filter(recipe -> recipe.getType() == type).collect(Collectors.toSet()) : Collections.emptySet();
    }

    @OnlyIn(Dist.CLIENT)
    public static Set<IRecipe<?>> findRecipeByType(IRecipeType<?> type) {
        ClientWorld world = Minecraft.getInstance().level;
        return world != null ? world.getRecipeManager().getRecipes().stream().filter(recipe -> recipe.getType() == type).collect(Collectors.toSet()) : Collections.emptySet();
    }

    @Override
    public IEnergyHandler getEnergyHandler() {
        return handler;
    }

    private PipeNetwork network;

    @Override
    public PipeNetwork getNetwork(Direction side) { return network; }
    @Override
    public void setNetwork(Direction side, PipeNetwork network) { this.network = network; }

    @Override
    public boolean canConnect(Direction side, Capability<?> capability) {
        return getCapability(capability, side).isPresent();
    }

    @Override
    public Type getSideType(Direction side, Capability<?> capability) {
        if(capability == ModdedCapabilities.ENERGY) return getCapability(ModdedCapabilities.ENERGY, side).isPresent() ? Type.RECEIVER : Type.NONE;
        return Type.NONE;
    }

    @Override
    public Type getMainType(Capability<?> capability) {
        if(capability == ModdedCapabilities.ENERGY) return Type.RECEIVER;
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
