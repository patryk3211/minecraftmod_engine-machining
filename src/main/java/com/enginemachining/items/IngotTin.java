package com.enginemachining.items;

import net.minecraft.item.Item;

public class IngotTin extends Item {
    public IngotTin() {
        super(new Properties().group(ModdedItemGroups.metals));
        setRegistryName("enginemachining:ingot_tin");
    }
}
