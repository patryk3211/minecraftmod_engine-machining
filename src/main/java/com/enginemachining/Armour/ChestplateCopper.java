package com.enginemachining.Armour;

import com.enginemachining.items.ModdedItemGroups;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;

public class ChestplateCopper extends ArmorItem {
    public ChestplateCopper() {
        super(ModArmorMaterial.copper, EquipmentSlotType.CHEST, new Properties().group(ModdedItemGroups.tools));
        setRegistryName("enginemachining:chestplate_copper");
    }
}
