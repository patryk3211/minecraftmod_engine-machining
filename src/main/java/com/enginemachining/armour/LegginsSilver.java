package com.enginemachining.armour;

import com.enginemachining.items.ModdedItemGroups;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;

public class LegginsSilver extends ArmorItem {
    public LegginsSilver() {
        super(ModArmorMaterial.silver, EquipmentSlotType.LEGS, new Properties().group(ModdedItemGroups.tools));
    }
}
