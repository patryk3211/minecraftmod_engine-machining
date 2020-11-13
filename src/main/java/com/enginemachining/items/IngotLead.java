package com.enginemachining.items;

import net.minecraft.item.Item;

public class IngotLead extends Item {
    public IngotLead() {
        super(new Properties().group(ModdedItemGroups.metals));
        setRegistryName("enginemachining:ingot_lead");
    }
}
