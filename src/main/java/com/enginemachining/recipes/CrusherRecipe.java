package com.enginemachining.recipes;

import com.enginemachining.items.ModdedItems;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.realmsclient.util.JsonUtils;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;

public class CrusherRecipe implements IRecipe<IInventory> {
    private ResourceLocation id;
    private String group;
    private Ingredient ingredient;
    private ItemStack result;
    private int time;

    public CrusherRecipe(ResourceLocation id, String group, Ingredient ingredient, ItemStack result, int time) {
        this.id = id;
        this.group = group;
        this.ingredient = ingredient;
        this.result = result;
        this.time = time;
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return ingredient.test(inv.getStackInSlot(0));
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return result.copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return result;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModdedRecipeSerializers.crusherRecipeSerializer;
    }

    @Override
    public IRecipeType<?> getType() {
        return ModdedRecipeTypes.crushing;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.from(ingredient);
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<CrusherRecipe> {

        @Override
        public CrusherRecipe read(ResourceLocation recipeId, JsonObject json) {
            JsonObject inputItemStack = json.getAsJsonObject("input");
            if(inputItemStack == null) throw new JsonParseException("Crushing crafting must contain an input item");
            JsonObject outputItemStack = json.getAsJsonObject("result");
            if(outputItemStack == null) throw new JsonParseException("Crushing crafting must contain a result item");
            Item inputItem = JSONUtils.getItem(inputItemStack, "item");
            Item outputItem = JSONUtils.getItem(outputItemStack, "item");
            int inputAmount = JSONUtils.getInt(inputItemStack, "count", 1);
            int outputAmount = JSONUtils.getInt(outputItemStack, "count", 1);
            int time = JSONUtils.getInt(json, "time", 100);
            String group = JSONUtils.getString(json, "group", "");
            return new CrusherRecipe(recipeId, group, Ingredient.fromStacks(new ItemStack(inputItem, inputAmount)), new ItemStack(outputItem, outputAmount), time);
        }

        @Nullable
        @Override
        public CrusherRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            //throw new UnsupportedOperationException("Reading a recipe from a PacketBuffer is currently not supported.");
            /*ItemStack input = buffer.readItemStack();
            ItemStack output = buffer.readItemStack();
            int time = buffer.readInt();
            return new CrusherRecipe(recipeId, "", Ingredient.fromStacks(input), output, time);*/
            //System.out.println("hellohere2");
            return new CrusherRecipe(recipeId, "", Ingredient.fromItems(ModdedItems.ingot_lead), new ItemStack(ModdedItems.dust_lead), 100);
        }

        @Override
        public void write(PacketBuffer buffer, CrusherRecipe recipe) {

        }
    }
}
