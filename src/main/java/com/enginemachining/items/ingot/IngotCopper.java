package com.enginemachining.items.ingot;

import com.enginemachining.items.ModdedItemGroups;
import net.minecraft.item.Item;

public class IngotCopper extends Item {
    public IngotCopper() {
        super(new Properties().group(ModdedItemGroups.metals));
        setRegistryName("enginemachining:ingot_copper");
    }
}
