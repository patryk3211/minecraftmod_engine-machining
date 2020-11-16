package com.enginemachining.tools.silver;

import com.enginemachining.items.ModdedItemGroups;
import com.enginemachining.tools.ModItemTier;
import net.minecraft.item.ShovelItem;

public class ShovelSilver extends ShovelItem {
    public ShovelSilver() {
        super(ModItemTier.silver, 0.5f, -3.0f, new Properties().group(ModdedItemGroups.tools));
        setRegistryName("enginemachining:shovel_silver");
    }
}
