package com.enginemachining.items.dust;

import com.enginemachining.items.ModdedItemGroups;
import net.minecraft.item.Item;

public class DustIron extends Item {
    public DustIron() {
        super(new Properties().group(ModdedItemGroups.metals));
        setRegistryName("enginemachining:dust_iron");
    }
}
