package com.enginemachining.armour;

import com.enginemachining.items.ModdedItemGroups;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;

public class BootsCopper extends ArmorItem {
    public BootsCopper() {
        super(ModArmorMaterial.copper, EquipmentSlotType.FEET, new Properties().tab(ModdedItemGroups.tools));
    }
}
