package com.enginemachining.items.Nuggetsy;

import com.enginemachining.items.ModdedItemGroups;
import net.minecraft.item.Item;

public class NuggetCopper extends Item {
    public NuggetCopper() {
        super(new Properties().group(ModdedItemGroups.metals));
        setRegistryName("enginemachining:nugget_copper");
    }
}
