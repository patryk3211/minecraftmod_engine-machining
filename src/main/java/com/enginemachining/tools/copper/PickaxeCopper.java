package com.enginemachining.tools.copper;

import com.enginemachining.items.ModdedItemGroups;
import com.enginemachining.tools.ModItemTier;
import net.minecraft.item.PickaxeItem;

public class PickaxeCopper extends PickaxeItem {
    public PickaxeCopper() {
        super(ModItemTier.copper, 1, -2.8f, new Properties().tab(ModdedItemGroups.tools));
    }
}
