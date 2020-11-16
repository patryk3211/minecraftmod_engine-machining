package com.enginemachining.tools.silver;

import com.enginemachining.items.ModdedItemGroups;
import com.enginemachining.tools.ModItemTier;
import net.minecraft.item.HoeItem;

public class HoeSilver extends HoeItem {
    public HoeSilver() {
        super(ModItemTier.silver, -1, -3,new Properties().group(ModdedItemGroups.tools));
        setRegistryName("enginemachining:hoe_silver");
    }
}
