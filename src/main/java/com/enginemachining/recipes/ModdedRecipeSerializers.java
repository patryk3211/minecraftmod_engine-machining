package com.enginemachining.recipes;

import com.enginemachining.EngineMachiningMod;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModdedRecipeSerializers {
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, EngineMachiningMod.MOD_ID);

    //public static SpecialRecipeSerializer<BatteryDisposableCraftingRecipe> disposableBatteryCraftingSerializer = IRecipeSerializer.register("crafting_special_battery_disposable", new SpecialRecipeSerializer<>(BatteryDisposableCraftingRecipe::new));
    public static RegistryObject<IRecipeSerializer<CrusherRecipe>> crusherRecipeSerializer = RECIPES.register("crushing", CrusherRecipe.Serializer::new);
}
