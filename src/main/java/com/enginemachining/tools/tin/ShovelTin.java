package com.enginemachining.tools.tin;

import com.enginemachining.items.ModdedItemGroups;
import com.enginemachining.tools.ModItemTier;
import net.minecraft.item.ShovelItem;

public class ShovelTin extends ShovelItem {
    public ShovelTin() {
        super(ModItemTier.tin, 0.5f, -3.0f, new Properties().tab(ModdedItemGroups.tools));
    }
}
