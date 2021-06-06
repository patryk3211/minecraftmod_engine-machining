package com.enginemachining.armour;

import com.enginemachining.items.ModdedItems;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

import java.util.function.Supplier;

public enum ModArmorMaterial implements IArmorMaterial {

    copper("enginemachining:copper", 25, new int[] { 2, 5, 6, 2 }, 9,
            SoundEvents.ARMOR_EQUIP_GENERIC, 0.0F, () -> { return Ingredient.of(ModdedItems.ingot_copper.get()); }),
    silver("enginemachining:silver", 25, new int[] { 2, 6, 5, 2 }, 22,
            SoundEvents.ARMOR_EQUIP_GENERIC, 0.0F, () -> { return  Ingredient.of(ModdedItems.ingot_silver.get()); });

    private static final int[] MAX_DAMAGE_ARRAY = new int[] { 11, 16, 15, 13 };
    private final String name;
    private final int maxDamageFactor;
    private final int[] damageReductionAmountArray;
    private final int enchantability;
    private final SoundEvent soundEvent;
    private final float toughness;
    private final Supplier<Ingredient> repairMaterial;

    ModArmorMaterial(String name, int maxDamageFactor, int[] damageReductionAmountArray, int enchantability,
                     SoundEvent soundEvent, float toughness, Supplier<Ingredient> repairMaterial) {
         this.name = name;
         this.maxDamageFactor = maxDamageFactor;
         this.damageReductionAmountArray = damageReductionAmountArray;
         this.enchantability = enchantability;
         this.soundEvent = soundEvent;
         this.toughness = toughness;
         this.repairMaterial = repairMaterial;
    }



    @Override
    public int getDurabilityForSlot(EquipmentSlotType slotIn) {
        return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * this.maxDamageFactor;
    }

    @Override
    public int getDefenseForSlot(EquipmentSlotType slotIn) {
        return this.damageReductionAmountArray[slotIn.getIndex()];
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.soundEvent;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairMaterial.get();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return 0;
    }
}
