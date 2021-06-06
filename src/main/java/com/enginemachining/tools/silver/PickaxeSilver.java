package com.enginemachining.tools.silver;

import com.enginemachining.items.ModdedItemGroups;
import com.enginemachining.tools.ModItemTier;
import net.minecraft.item.PickaxeItem;

public class PickaxeSilver extends PickaxeItem {
    public PickaxeSilver() {
        super(ModItemTier.silver, 2, -2.8f, new Properties().tab(ModdedItemGroups.tools));
    }
}
