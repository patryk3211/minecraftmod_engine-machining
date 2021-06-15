package com.enginemachining.tools.aluminium;

import com.enginemachining.items.ModdedItemGroups;
import com.enginemachining.tools.ModItemTier;
import net.minecraft.item.ShovelItem;

public class ShovelAluminium extends ShovelItem {
    public ShovelAluminium() {
        super(ModItemTier.aluminium, 1, -3.0f, new Properties().tab(ModdedItemGroups.tools));
    }
}
