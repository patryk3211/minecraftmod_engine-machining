package com.enginemachining.tools.aluminium;

import com.enginemachining.items.ModdedItemGroups;
import com.enginemachining.tools.ModItemTier;
import net.minecraft.item.SwordItem;

public class SwordAluminium extends SwordItem {
    public SwordAluminium() {
        super(ModItemTier.aluminium, 3, -2.4f, new Properties().tab(ModdedItemGroups.tools));
    }
}
