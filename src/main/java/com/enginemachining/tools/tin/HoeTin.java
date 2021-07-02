package com.enginemachining.tools.tin;

import com.enginemachining.items.ModdedItemGroups;
import com.enginemachining.tools.ModItemTier;
import net.minecraft.item.HoeItem;

public class HoeTin extends HoeItem {
    public HoeTin() {
        super(ModItemTier.silver, -1, -3,new Properties().tab(ModdedItemGroups.tools));
    }
}
