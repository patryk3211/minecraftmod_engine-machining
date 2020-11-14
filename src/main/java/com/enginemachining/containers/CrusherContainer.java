package com.enginemachining.containers;

import com.enginemachining.blocks.Crusher;
import com.enginemachining.blocks.ModdedBlocks;
import com.enginemachining.screens.CrusherScreen;
import com.enginemachining.tileentities.CrusherTile;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

public class CrusherContainer extends Container {
    public CrusherTile tileEntity;
    public PlayerInventory inv;

    private IWorldPosCallable callable;

    public CrusherContainer(int id, PlayerInventory inv, CrusherTile tile) {
        super(ModdedContainers.crusher, id);
        this.inv = inv;
        this.tileEntity = tile;

        callable = IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos());

        LayoutInventory();

        addSlot(new Slot(tileEntity.blockInventory, 0, 45, 34));
        addSlot(new Slot(tileEntity.blockInventory, 1, 114, 34));
        addSlot(new Slot(tileEntity.blockInventory, 2, 146, 61));
    }

    public CrusherContainer(int id, PlayerInventory inv, PacketBuffer data) {
        this(id, inv, GetTileEntity(inv, data));
    }

    private static CrusherTile GetTileEntity(PlayerInventory inv, PacketBuffer data) {
        Objects.requireNonNull(inv, "Player Inventory cannot be null");
        Objects.requireNonNull(data, "Data cannot be null");

        TileEntity tileAtPos = inv.player.world.getTileEntity(data.readBlockPos());
        if(tileAtPos instanceof CrusherTile) {
            return (CrusherTile)tileAtPos;
        }

        return null;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(callable, playerIn, ModdedBlocks.crusher);
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
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        System.out.println(index);
        return ItemStack.EMPTY;
    }
}
