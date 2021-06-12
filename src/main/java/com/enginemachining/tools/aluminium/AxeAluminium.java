package com.enginemachining.tools.aluminium;

import com.enginemachining.items.ModdedItemGroups;
import com.enginemachining.tools.ModItemTier;
import net.minecraft.item.AxeItem;

public class AxeAluminium extends AxeItem {
    public AxeAluminium() {
        super(ModItemTier.aluminium, 5.5f, -3.2f, new Properties().tab(ModdedItemGroups.tools));
    }
}
