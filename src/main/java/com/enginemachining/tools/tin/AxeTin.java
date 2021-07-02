package com.enginemachining.tools.tin;

import com.enginemachining.items.ModdedItemGroups;
import com.enginemachining.tools.ModItemTier;
import net.minecraft.item.AxeItem;

public class AxeTin extends AxeItem {
    public AxeTin() {
        super(ModItemTier.tin, 5, -3.2f, new Properties().tab(ModdedItemGroups.tools));
    }
}
