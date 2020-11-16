package com.enginemachining.Armour;

import com.enginemachining.items.ModdedItemGroups;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;

public class BootsSilver extends ArmorItem {
    public BootsSilver() {
        super(ModArmorMaterial.silver, EquipmentSlotType.FEET, new Properties().group(ModdedItemGroups.tools));
        setRegistryName("enginemachining:boots_silver");
    }
}
