package com.enginemachining.tools;

import com.enginemachining.items.ModdedItemGroups;
import net.minecraft.item.IItemTier;
import net.minecraft.item.SwordItem;

public class SwordCopper extends SwordItem {
    public SwordCopper() {
        super(ModItemTier.copper, 3, -2.4f, new Properties().group(ModdedItemGroups.tools));
        setRegistryName("enginemachining:sword_copper");
    }
}
