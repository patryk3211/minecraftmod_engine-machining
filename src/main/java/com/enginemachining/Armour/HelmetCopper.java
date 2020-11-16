package com.enginemachining.Armour;

import com.enginemachining.items.ModdedItemGroups;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;

public class HelmetCopper extends ArmorItem {
    public HelmetCopper() {
        super(ModArmorMaterial.copper, EquipmentSlotType.HEAD, new Properties().group(ModdedItemGroups.tools));
        setRegistryName("enginemachining:helmet_copper");
    }
}
