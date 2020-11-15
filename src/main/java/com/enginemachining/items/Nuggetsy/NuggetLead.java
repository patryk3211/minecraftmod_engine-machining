package com.enginemachining.items.Nuggetsy;

import com.enginemachining.items.ModdedItemGroups;
import net.minecraft.item.Item;

public class NuggetLead extends Item {
    public NuggetLead() {
        super(new Properties().group(ModdedItemGroups.metals));
        setRegistryName("enginemachining:nugget_lead");
    }
}
