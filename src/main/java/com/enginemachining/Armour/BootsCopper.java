package com.enginemachining.Armour;

import com.enginemachining.items.ModdedItemGroups;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;

public class BootsCopper extends ArmorItem {
    public BootsCopper() {
        super(ModArmorMaterial.copper, EquipmentSlotType.FEET, new Properties().group(ModdedItemGroups.tools));
        setRegistryName("enginemachining:boots_copper");
    }
}
