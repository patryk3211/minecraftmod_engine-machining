package com.enginemachining.items.dust;

import com.enginemachining.items.ModdedItemGroups;
import net.minecraft.item.Item;

public class DustCoal extends Item {
    public DustCoal() {
        super(new Properties().group(ModdedItemGroups.metals));
        setRegistryName("enginemachining:dust_coal");
    }
}
