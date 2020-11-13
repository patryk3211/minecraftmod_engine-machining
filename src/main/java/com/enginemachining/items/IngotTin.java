package com.enginemachining.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class IngotTin extends Item {
    public IngotTin() {
        super(new Properties().group(ModdedItemGroups.ores));
        setRegistryName("enginemachining:ingot_tin");
    }
}
