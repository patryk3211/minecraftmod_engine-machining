package com.enginemachining.Armour;

import com.enginemachining.items.ModdedItemGroups;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;

public class ChestplateSilver extends ArmorItem {
    public ChestplateSilver() {
        super(ModArmorMaterial.silver, EquipmentSlotType.CHEST, new Properties().group(ModdedItemGroups.tools));
        setRegistryName("enginemachining:chestplate_silver");
    }
}
