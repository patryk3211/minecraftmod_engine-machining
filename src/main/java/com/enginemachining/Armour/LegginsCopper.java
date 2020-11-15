package com.enginemachining.Armour;

import com.enginemachining.items.ModdedItemGroups;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;

public class LegginsCopper extends ArmorItem {
    public LegginsCopper() {
        super(ModArmorMaterial.copper, EquipmentSlotType.LEGS, new Properties().group(ModdedItemGroups.tools));
        setRegistryName("enginemachining:leggins_copper");
    }
}
