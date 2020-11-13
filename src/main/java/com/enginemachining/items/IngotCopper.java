package com.enginemachining.items;

import net.minecraft.item.Item;

public class IngotCopper extends Item {
    public IngotCopper() {
        super(new Properties().group(ModdedItemGroups.metals));
        setRegistryName("enginemachining:ingot_copper");
    }
}
