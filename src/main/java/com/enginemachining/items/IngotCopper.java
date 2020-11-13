package com.enginemachining.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class IngotCopper extends Item {
    public IngotCopper() {
        super(new Properties().group(ModdedItemGroups.ores));
        setRegistryName("enginemachining:ingot_copper");
    }
}
