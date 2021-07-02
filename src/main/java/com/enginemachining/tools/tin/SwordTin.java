package com.enginemachining.tools.tin;

import com.enginemachining.items.ModdedItemGroups;
import com.enginemachining.tools.ModItemTier;
import net.minecraft.item.SwordItem;

public class SwordTin extends SwordItem {
    public SwordTin() {
        super(ModItemTier.tin, 3, -2.4f, new Properties().tab(ModdedItemGroups.tools));
    }
}
