package com.enginemachining.items.Nuggetsy;

import com.enginemachining.items.ModdedItemGroups;
import net.minecraft.item.Item;

public class NuggetSilver extends Item {
    public NuggetSilver() {
        super(new Properties().group(ModdedItemGroups.metals));
        setRegistryName("enginemachining:nugget_silver");
    }
}