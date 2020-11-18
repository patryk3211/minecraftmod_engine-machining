package com.enginemachining.armour;

import com.enginemachining.items.ModdedItemGroups;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;

public class HelmetSilver extends ArmorItem {
    public HelmetSilver() {
        super(ModArmorMaterial.silver, EquipmentSlotType.HEAD, new Properties().group(ModdedItemGroups.tools));
    }
}
