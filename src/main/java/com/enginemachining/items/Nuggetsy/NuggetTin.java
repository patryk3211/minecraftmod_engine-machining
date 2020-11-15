package com.enginemachining.items.Nuggetsy;

import com.enginemachining.items.ModdedItemGroups;
import net.minecraft.item.Item;

public class NuggetTin extends Item {
    public NuggetTin() {
        super(new Properties().group(ModdedItemGroups.metals));
        setRegistryName("enginemachining:nugget_tin");
    }
}
