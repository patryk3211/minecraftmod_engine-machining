package com.enginemachining.items;

import net.minecraft.item.Item;

public class IngotSilver extends Item {
    public IngotSilver() {
        super(new Properties().group(ModdedItemGroups.metals));
        setRegistryName("enginemachining:ingot_silver");
    }
}
