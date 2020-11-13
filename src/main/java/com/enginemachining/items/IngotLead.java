package com.enginemachining.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class IngotLead extends Item {
    public IngotLead() {
        super(new Properties().group(ModdedItemGroups.ores));
        setRegistryName("enginemachining:ingot_lead");
    }
}
