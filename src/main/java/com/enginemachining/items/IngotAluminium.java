package com.enginemachining.items;

import net.minecraft.item.Item;

public class IngotAluminium extends Item {
    public IngotAluminium() {
        super(new Properties().group(ModdedItemGroups.metals));
        setRegistryName("enginemachining:ingot_aluminium");
    }
}
