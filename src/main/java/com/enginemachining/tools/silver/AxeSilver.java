package com.enginemachining.tools.silver;

import com.enginemachining.items.ModdedItemGroups;
import com.enginemachining.tools.ModItemTier;
import net.minecraft.item.AxeItem;

public class AxeSilver extends AxeItem {
    public AxeSilver() {
        super(ModItemTier.silver, 5, -3.2f, new Properties().tab(ModdedItemGroups.tools));
    }
}
