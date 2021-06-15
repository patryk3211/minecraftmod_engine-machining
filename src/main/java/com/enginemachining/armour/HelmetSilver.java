package com.enginemachining.armour;

import com.enginemachining.items.ModdedItemGroups;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class HelmetSilver extends ArmorItem {
    public HelmetSilver() {
        super(ModArmorMaterial.silver, EquipmentSlotType.HEAD, new Properties().tab(ModdedItemGroups.tools));
    }
    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return "enginemachining:textures/item/armor/armor_silver.png";
    }
}
