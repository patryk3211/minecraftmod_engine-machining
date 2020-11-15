package com.enginemachining.items.Nuggetsy;

import com.enginemachining.items.ModdedItemGroups;
import net.minecraft.item.Item;

public class NuggetNickel extends Item {
    public NuggetNickel() {
        super(new Properties().group(ModdedItemGroups.metals));
        setRegistryName("enginemachining:nugget_nickel");
    }
}
