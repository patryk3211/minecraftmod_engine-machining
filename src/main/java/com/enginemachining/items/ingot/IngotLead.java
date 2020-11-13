package com.enginemachining.items.ingot;

import com.enginemachining.items.ModdedItemGroups;
import net.minecraft.item.Item;

public class IngotLead extends Item {
    public IngotLead() {
        super(new Properties().group(ModdedItemGroups.metals));
        setRegistryName("enginemachining:ingot_lead");
    }
}
