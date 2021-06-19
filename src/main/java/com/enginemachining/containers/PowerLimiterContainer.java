package com.enginemachining.containers;

import com.enginemachining.tileentities.CrusherTile;
import com.enginemachining.tileentities.PowerLimiterTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;

import javax.annotation.Nullable;
import java.util.Objects;

public class PowerLimiterContainer extends Container {
    public PowerLimiterTile tileEntity;
    public PlayerInventory inventory;

    public IIntArray trackedArray;

    public PowerLimiterContainer(int id, PlayerInventory inventory, PowerLimiterTile tile) {
        super(ModdedContainers.power_limiter.get(), id);

        trackedArray = new IntArray(5);
        if(!tile.getLevel().isClientSide()) trackedArray = tile.data;
        addDataSlots(trackedArray);

        this.tileEntity = tile;
        this.inventory = inventory;
    }

    public PowerLimiterContainer(int id, PlayerInventory inventory, PacketBuffer buffer) {
        this(id, inventory, GetTileEntity(inventory, buffer));
    }

    private static PowerLimiterTile GetTileEntity(PlayerInventory inv, PacketBuffer data) {
        Objects.requireNonNull(inv, "Player Inventory cannot be null");
        Objects.requireNonNull(data, "Data cannot be null");

        TileEntity tileAtPos = inv.player.level.getBlockEntity(data.readBlockPos());
        if(tileAtPos instanceof PowerLimiterTile) {
            return (PowerLimiterTile)tileAtPos;
        }

        return null;
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return true;
    }
}
