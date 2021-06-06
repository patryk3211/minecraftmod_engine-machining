package com.enginemachining.armour;

import com.enginemachining.items.ModdedItemGroups;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;

public class ChestplateCopper extends ArmorItem {
    public ChestplateCopper() {
        super(ModArmorMaterial.copper, EquipmentSlotType.CHEST, new Properties().tab(ModdedItemGroups.tools));
    }
}
