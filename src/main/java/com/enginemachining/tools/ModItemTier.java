package com.enginemachining.tools;

import com.enginemachining.items.ModdedItems;
import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;

public enum ModItemTier implements IItemTier {

    copper(2, 200, 5.0f, 1.0f, 9, Ingredient.of(ModdedItems.ingot_copper.get())),
    silver(2, 100, 9.0f, 1.0f, 22, Ingredient.of(ModdedItems.ingot_silver.get()));

    private final int harvestLevel;
    private final int maxUses;
    private final float efficiency;
    private final float attackDamage;
    private final int enchantability;
    private final Ingredient repairMaterial;

    ModItemTier(int harvestLevel, int maxUses, float efficiency, float attackDamage, int enchantability, Ingredient repairMaterial) {
        this.harvestLevel = harvestLevel;
        this.maxUses = maxUses;
        this.efficiency = efficiency;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairMaterial = repairMaterial;
    }

    @Override
    public int getUses() {
        return maxUses;
    }

    @Override
    public float getSpeed() {
        return efficiency;
    }

    @Override
    public float getAttackDamageBonus() {
        return attackDamage;
    }

    @Override
    public int getLevel() {
        return harvestLevel;
    }

    @Override
    public int getEnchantmentValue() {
        return enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairMaterial;
    }
}
