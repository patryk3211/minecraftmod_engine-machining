package com.enginemachining.tools.copper;

import com.enginemachining.items.ModdedItemGroups;
import com.enginemachining.tools.ModItemTier;
import net.minecraft.item.HoeItem;

public class HoeCopper extends HoeItem {
    public HoeCopper() {
        super(ModItemTier.copper, -1, -2,new Properties().group(ModdedItemGroups.tools));
        setRegistryName("enginemachining:hoe_copper");
    }
}