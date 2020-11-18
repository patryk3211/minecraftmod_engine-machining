package com.enginemachining.tools.silver;

import com.enginemachining.items.ModdedItemGroups;
import com.enginemachining.tools.ModItemTier;
import net.minecraft.item.SwordItem;

public class SwordSilver extends SwordItem {
    public SwordSilver() {
        super(ModItemTier.silver, 3, -2.4f, new Properties().group(ModdedItemGroups.tools));
    }
}
