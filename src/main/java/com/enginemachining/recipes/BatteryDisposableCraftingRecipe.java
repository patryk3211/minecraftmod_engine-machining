package com.enginemachining.recipes;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class BatteryDisposableCraftingRecipe extends SpecialRecipe {

    public BatteryDisposableCraftingRecipe(ResourceLocation idIn) {
        super(idIn);
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        //TODO: Need an aluminium nugget to implement this crafting
        return false;
    }

    @Override
    public ItemStack assemble(CraftingInventory p_77572_1_) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 3 && height >= 3;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return null;
    }
}
