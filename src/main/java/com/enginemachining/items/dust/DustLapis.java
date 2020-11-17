package com.enginemachining.items.dust;

import com.enginemachining.items.ModdedItemGroups;
import net.minecraft.item.Item;

public class DustLapis extends Item {
    public DustLapis() {
        super(new Properties().group(ModdedItemGroups.metals));
        setRegistryName("enginemachining:dust_lapis");
    }
}
