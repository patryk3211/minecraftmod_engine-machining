package com.enginemachining.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class IngotSilver extends Item {
    public IngotSilver() {
        super(new Properties().group(ModdedItemGroups.ores));
        setRegistryName("enginemachining:ingot_silver");
    }
}
