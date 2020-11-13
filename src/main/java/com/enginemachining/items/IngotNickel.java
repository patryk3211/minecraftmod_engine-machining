package com.enginemachining.items;

import net.minecraft.item.Item;

public class IngotNickel extends Item {
    public IngotNickel() {
        super(new Properties().group(ModdedItemGroups.metals));
        setRegistryName("enginemachining:ingot_nickel");
    }
}
