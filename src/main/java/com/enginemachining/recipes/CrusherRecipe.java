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
    private NonNullList<Ingredient> ingredients;
    private ItemStack result;
    private int time;

    private ItemStack inputStack;

    public CrusherRecipe(ResourceLocation id, String group, Ingredient ingredient, ItemStack result, int time) {
        this.id = id;
        this.group = group;
        this.ingredients = NonNullList.create();
        ingredients.add(0, ingredient);
        this.result = result;
        this.time = time;
    }

    public CrusherRecipe setInputStack(ItemStack stack) { inputStack = stack; return this; }
    public ItemStack getInputStack() { return inputStack; }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return ingredients.get(0).test(inv.getStackInSlot(0));
    }

    public int getTime() { return time; }

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
        return ingredients;
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
            ItemStack inStack = new ItemStack(inputItem, inputAmount);
            return new CrusherRecipe(recipeId, group, Ingredient.fromStacks(inStack), new ItemStack(outputItem, outputAmount), time).setInputStack(inStack);
        }

        @Nullable
        @Override
        public CrusherRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            String group = buffer.readString();
            ItemStack input = buffer.readItemStack();
            ItemStack output = buffer.readItemStack();
            int time = buffer.readInt();
            return new CrusherRecipe(recipeId, group, Ingredient.fromStacks(input), output, time);
            //System.out.println("hellohere2");
            //return new CrusherRecipe(recipeId, "", Ingredient.fromItems(ModdedItems.ingot_lead), new ItemStack(ModdedItems.dust_lead), 100);
        }

        @Override
        public void write(PacketBuffer buffer, CrusherRecipe recipe) {
            buffer.writeString(recipe.group);
            buffer.writeItemStack(recipe.getInputStack());
            buffer.writeItemStack(recipe.getRecipeOutput());
            buffer.writeInt(recipe.time);
        }
    }
}
