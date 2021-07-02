package com.enginemachining.tools.tin;

import com.enginemachining.items.ModdedItemGroups;
import com.enginemachining.tools.ModItemTier;
import net.minecraft.item.PickaxeItem;

public class PickaxeTin extends PickaxeItem {
    public PickaxeTin() {
        super(ModItemTier.tin, 1, -2.8f, new Properties().tab(ModdedItemGroups.tools));
    }
}
