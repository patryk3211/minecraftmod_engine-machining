package com.enginemachining.tileentities;

import com.enginemachining.blocks.Crusher;
import com.enginemachining.blocks.ModdedBlocks;
import com.enginemachining.containers.CrusherContainer;
import com.enginemachining.handlers.EnergyReceiverHandler;
import com.enginemachining.items.ModdedItemTags;
import com.enginemachining.items.ModdedItems;
import com.enginemachining.recipes.CrusherRecipe;
import com.enginemachining.recipes.ModdedRecipeTypes;
import com.sun.jna.platform.unix.X11;
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
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.antlr.v4.runtime.misc.NotNull;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class CrusherTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {
    private CrusherContainer container;

    private NonNullList<ItemStack> slots = NonNullList.withSize(3, ItemStack.EMPTY);
    public ISidedInventory blockInventory;
    private LazyOptional<? extends IItemHandler>[] itemHandlers;
    private LazyOptional<IEnergyStorage> energyHandler = LazyOptional.of(() -> {
        EnergyReceiverHandler erh = new EnergyReceiverHandler(10000);
        return erh;
    });

    boolean enabled;

    int power;
    public static final int HEAT_MAX = 1000;
    static final int ENERGY_PER_POWER = 10;
    static final int COOLDOWN_PER_TICK = 1;

    public IIntArray trackedData = new IIntArray() {
        @Override
        public int get(int index) {
            AtomicInteger ret;
            switch (index) {
                case 0:
                    ret = new AtomicInteger();
                    energyHandler.ifPresent((handler) -> {
                        ret.set(handler.getEnergyStored());
                    });
                    return ret.get();
                case 1:
                    ret = new AtomicInteger();
                    energyHandler.ifPresent((handler) -> {
                        ret.set(handler.getMaxEnergyStored());
                    });
                    return ret.get();
                case 2:
                    return enabled ? 1 : 0;
                case 3:
                    return power;
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
        public int size() {
            return 4;
        }
    };

    public CrusherTile() {
        super(ModdedTileEntities.crusher);
        blockInventory = new ISidedInventory() {
            @Override
            public int[] getSlotsForFace(Direction side) {
                return new int[] { 0, 1, 2 };
            }

            @Override
            public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
                return slots.get(index).isEmpty() || (slots.get(index).getCount() < 64 && slots.get(index).getItem() == itemStackIn.getItem());
            }

            @Override
            public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
                return !slots.get(index).isEmpty() && slots.get(index).getCount() >= stack.getCount() && stack.getItem() == slots.get(index).getItem();
            }

            @Override
            public int getSizeInventory() {
                return slots.size();
            }

            @Override
            public boolean isEmpty() {
                return slots.get(0).isEmpty() && slots.get(1).isEmpty() && slots.get(2).isEmpty();
            }

            @Override
            public ItemStack getStackInSlot(int index) {
                if(index >= getSizeInventory()) return null;
                return slots.get(index);
            }

            @Override
            public ItemStack decrStackSize(int index, int count) {
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
            public ItemStack removeStackFromSlot(int index) {
                ItemStack ret = slots.get(index);
                slots.set(index, ItemStack.EMPTY);
                return ret;
            }

            @Override
            public void setInventorySlotContents(int index, ItemStack stack) {
                slots.set(index, stack);
            }

            @Override
            public void markDirty() {
                CrusherTile.this.markDirty();
            }

            @Override
            public boolean isUsableByPlayer(PlayerEntity player) {
                return true;
            }

            @Override
            public void clear() {
                slots.set(0, ItemStack.EMPTY);
                slots.set(1, ItemStack.EMPTY);
                slots.set(2, ItemStack.EMPTY);
            }
        };
        itemHandlers = SidedInvWrapper.create(blockInventory, Direction.NORTH);
        enabled = false;
        power = 0;
    }

    @Override
    public void tick() {
        if (!world.isRemote) {
            if(power > 0) {
                power -= COOLDOWN_PER_TICK;
            }
            energyHandler.ifPresent((handler) -> {
                Item bat = slots.get(2).getItem();
                if(bat == ModdedItems.battery_disposable) {
                    if(slots.get(2).getTag() == null || slots.get(2).getTag().getCompound("energy") == null) return;
                    CompoundNBT energyTag = slots.get(2).getTag().getCompound("energy");
                    int energyLeft = energyTag.getInt("charge");
                    int maxTransmit = energyTag.getInt("maxDischargeSpeed");

                    int received = 0;
                    if(energyLeft < maxTransmit) received = handler.receiveEnergy(energyLeft, false);
                    else received = handler.receiveEnergy(maxTransmit, false);

                    if(received > 0) {
                        slots.get(2).getTag().getCompound("energy").putInt("charge", energyLeft - received);
                    }
                }
                if(enabled) {
                    int powerToMax = HEAT_MAX - power;
                    int wantedChange = Math.min(powerToMax, 10);
                    int energyUsage = handler.extractEnergy(wantedChange * ENERGY_PER_POWER, true);
                    int availableChange = energyUsage / ENERGY_PER_POWER;
                    handler.extractEnergy(availableChange * ENERGY_PER_POWER, false);
                    power += availableChange;
                }
            });
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
    public void read(BlockState state, CompoundNBT nbt) {
        ItemStackHelper.loadAllItems(nbt, slots);
        energyHandler.ifPresent((handler) -> {
            if(handler instanceof EnergyReceiverHandler) {
                ((EnergyReceiverHandler) handler).deserializeNBT(nbt.getCompound("energy"));
            }
        });
        enabled = nbt.getBoolean("enabled");
        power = Math.min(nbt.getInt("power"), HEAT_MAX);
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        ItemStackHelper.saveAllItems(compound, slots, true);
        energyHandler.ifPresent((handler) -> {
            if(handler instanceof EnergyReceiverHandler) {
                CompoundNBT nbt = ((EnergyReceiverHandler) handler).serializeNBT();
                compound.put("energy", nbt);
            }
        });
        compound.putBoolean("enabled", enabled);
        compound.putInt("power", power);
        return super.write(compound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        System.out.println("packet");
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putBoolean("enabled", enabled);
        SUpdateTileEntityPacket packet = new SUpdateTileEntityPacket(pos, -1, nbt);
        return packet;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return itemHandlers[0].cast();
        }
        if(cap == CapabilityEnergy.ENERGY && side != Direction.NORTH) {
            return energyHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Nullable
    private CrusherRecipe GetRecipe(ItemStack input) {
        if(input != null) {
            return null;
        }

        Set<IRecipe<?>> recipes = findRecipeByType(ModdedRecipeTypes.crushing, this.world);
        for(IRecipe<?> recipe : recipes) {
            CrusherRecipe cr = (CrusherRecipe)recipe;
            if(cr.getIngredients().get(0).test(input)) return cr;
        }

        return null;
    }

    public static Set<IRecipe<?>> findRecipeByType(IRecipeType<?> type, World world) {
        return world != null ? world.getRecipeManager().getRecipes().stream().filter(recipe -> recipe.getType() == type).collect(Collectors.toSet()) : Collections.emptySet();
    }

    @OnlyIn(Dist.CLIENT)
    public static Set<IRecipe<?>> findRecipeByType(IRecipeType<?> type) {
        ClientWorld world = Minecraft.getInstance().world;
        return world != null ? world.getRecipeManager().getRecipes().stream().filter(recipe -> recipe.getType() == type).collect(Collectors.toSet()) : Collections.emptySet();
    }
}
