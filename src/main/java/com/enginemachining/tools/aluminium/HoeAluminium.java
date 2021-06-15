package com.enginemachining.tools.aluminium;

import com.enginemachining.items.ModdedItemGroups;
import com.enginemachining.tools.ModItemTier;
import net.minecraft.item.HoeItem;

public class HoeAluminium extends HoeItem {
    public HoeAluminium() {
        super(ModItemTier.aluminium, -1, -1.5f,new Properties().tab(ModdedItemGroups.tools));
    }
}
