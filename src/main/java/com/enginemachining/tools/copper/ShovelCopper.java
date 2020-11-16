package com.enginemachining.tools.copper;

import com.enginemachining.items.ModdedItemGroups;
import com.enginemachining.tools.ModItemTier;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ShovelItem;

public class ShovelCopper extends ShovelItem {
    public ShovelCopper() {
        super(ModItemTier.copper, 2, -3.0f, new Properties().group(ModdedItemGroups.tools));
        setRegistryName("enginemachining:shovel_copper");
    }
}
