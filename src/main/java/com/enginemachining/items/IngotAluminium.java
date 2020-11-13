package com.enginemachining.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class IngotAluminium extends Item {
    public IngotAluminium() {
        super(new Properties().group(ModdedItemGroups.ores));
        setRegistryName("enginemachining:ingot_aluminium");
    }
}
