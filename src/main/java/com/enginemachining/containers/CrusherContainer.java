package com.enginemachining.containers;

import com.enginemachining.items.ModdedItems;
import com.enginemachining.tileentities.CrusherTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;

import java.util.Objects;

public class CrusherContainer extends Container {
    public CrusherTile tileEntity;
    public PlayerInventory inv;

    public IIntArray trackedArray;

    public CrusherContainer(int id, PlayerInventory inv, CrusherTile tile) {
        super(ModdedContainers.crusher.get(), id);
        this.inv = inv;
        this.tileEntity = tile;

        trackedArray = new IntArray(tileEntity.trackedData.getCount());

        if(!tileEntity.getLevel().isClientSide) {
            trackedArray = tileEntity.trackedData;
        }

        addDataSlots(trackedArray);

        LayoutInventory();

        addSlot(new Slot(tileEntity.blockInventory, 0, 45, 34));
        addSlot(new OutputSlot(tileEntity.blockInventory, 1, 114, 34));
        addSlot(new BatterySlot(tileEntity.blockInventory, 2, 146, 61));
    }

    private static class BatterySlot extends Slot {
        public BatterySlot(IInventory inv, int index, int xPos, int yPos) {
            super(inv, index, xPos, yPos);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.getItem() == ModdedItems.battery_disposable.get();
        }
    }

    private static class OutputSlot extends Slot {
        public OutputSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }
    }

    public CrusherContainer(int id, PlayerInventory inv, PacketBuffer data) {
        this(id, inv, GetTileEntity(inv, data));
    }

    private static CrusherTile GetTileEntity(PlayerInventory inv, PacketBuffer data) {
        Objects.requireNonNull(inv, "Player Inventory cannot be null");
        Objects.requireNonNull(data, "Data cannot be null");

        TileEntity tileAtPos = inv.player.level.getBlockEntity(data.readBlockPos());
        if(tileAtPos instanceof CrusherTile) {
            return (CrusherTile)tileAtPos;
        }

        return null;
    }

    @Override
    public boolean stillValid(PlayerEntity playerIn) {
        // TODO: Check if player is still within usable distance
        return true;
    }

    private void LayoutInventory() {
        AddRange(8, 84, 9, 3, 9, inv);

        AddBar(8, 142, 9, 0, inv);
    }

    private void AddRange(int xPos, int yPos, int horizontalCount, int verticalCount, int index, IInventory inventory) {
        for(int i = 0; i < verticalCount; i++) {
            AddBar(xPos, yPos+i*18, horizontalCount, i*horizontalCount+index, inventory);
        }
    }

    private void AddBar(int xPos, int yPos, int count, int index, IInventory inventory) {
        for(int i = 0; i < count; i++) {
            addSlot(new Slot(inventory, i+index, xPos+i*18, yPos));
        }
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        return ItemStack.EMPTY;
    }
}
