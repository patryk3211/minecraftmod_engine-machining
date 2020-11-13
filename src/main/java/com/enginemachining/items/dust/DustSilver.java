package com.enginemachining.items.dust;

import com.enginemachining.items.ModdedItemGroups;
import net.minecraft.item.Item;

public class DustSilver extends Item {
    public DustSilver() {
        super(new Properties().group(ModdedItemGroups.metals));
        setRegistryName("enginemachining:dust_silver");
    }
}
