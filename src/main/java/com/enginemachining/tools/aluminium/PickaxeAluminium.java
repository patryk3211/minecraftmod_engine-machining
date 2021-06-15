package com.enginemachining.tools.aluminium;

import com.enginemachining.items.ModdedItemGroups;
import com.enginemachining.tools.ModItemTier;
import net.minecraft.item.PickaxeItem;

public class PickaxeAluminium extends PickaxeItem {
    public PickaxeAluminium() {
        super(ModItemTier.aluminium, 1, -2.8f, new Properties().tab(ModdedItemGroups.tools));
    }
}
