package com.enginemachining.armour;

import com.enginemachining.items.ModdedItemGroups;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;

public class ChestplateSilver extends ArmorItem {
    public ChestplateSilver() {
        super(ModArmorMaterial.silver, EquipmentSlotType.CHEST, new Properties().tab(ModdedItemGroups.tools));
    }
}
