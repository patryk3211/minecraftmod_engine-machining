package com.enginemachining.blocks.COIN;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.AbstractFurnaceContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeBookCategory;
import net.minecraft.util.IIntArray;

public class Container extends AbstractFurnaceContainer {
    public Container(int id, PlayerInventory playerInventoryIn) {
        super(ContainerType.FURNACE, TU TEN RECIPE, RecipeBookCategory.FURNACE, id, playerInventoryIn);
    }

    public Container(int id, PlayerInventory playerInventoryIn, IInventory furnaceInventoryIn, IIntArray p_i50083_4_) {
        super(ContainerType.FURNACE, TU TEN RECIPE, RecipeBookCategory.FURNACE, id, playerInventoryIn, furnaceInventoryIn, p_i50083_4_);
    }
}
