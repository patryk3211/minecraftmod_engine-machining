package com.enginemachining.armour;

import com.enginemachining.items.ModdedItemGroups;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;

public class BootsSilver extends ArmorItem {
    public BootsSilver() {
        super(ModArmorMaterial.silver, EquipmentSlotType.FEET, new Properties().tab(ModdedItemGroups.tools));
    }


}
