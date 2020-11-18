package com.enginemachining.tools.copper;

import com.enginemachining.items.ModdedItemGroups;
import com.enginemachining.tools.ModItemTier;
import net.minecraft.item.SwordItem;

public class SwordCopper extends SwordItem {
    public SwordCopper() {
        super(ModItemTier.copper, 3, -2.4f, new Properties().group(ModdedItemGroups.tools));
    }
}
