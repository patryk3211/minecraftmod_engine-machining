package com.enginemachining.tools.copper;

import com.enginemachining.items.ModdedItemGroups;
import com.enginemachining.tools.ModItemTier;
import net.minecraft.item.AxeItem;

public class AxeCopper extends AxeItem {
    public AxeCopper() {
        super(ModItemTier.copper, 6, -3.2f, new Properties().tab(ModdedItemGroups.tools));
    }
}
