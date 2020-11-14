package com.enginemachining.tools;

import com.enginemachining.items.ModdedItemGroups;
import net.minecraft.item.HoeItem;
import net.minecraft.item.IItemTier;

public class HoeCopper extends HoeItem {
    public HoeCopper() {
        super(ModItemTier.copper, -1, -2,new Properties().group(ModdedItemGroups.tools));
        setRegistryName("enginemachining:hoe_copper");
    }
}
