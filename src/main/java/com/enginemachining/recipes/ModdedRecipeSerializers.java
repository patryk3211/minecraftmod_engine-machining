package com.enginemachining.recipes;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;

public class ModdedRecipeSerializers {
    //public static SpecialRecipeSerializer<BatteryDisposableCraftingRecipe> disposableBatteryCraftingSerializer = IRecipeSerializer.register("crafting_special_battery_disposable", new SpecialRecipeSerializer<>(BatteryDisposableCraftingRecipe::new));
    public static IRecipeSerializer<CrusherRecipe> crusherRecipeSerializer = new CrusherRecipe.Serializer();
}
