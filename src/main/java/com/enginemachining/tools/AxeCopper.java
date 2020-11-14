package com.enginemachining.tools;

import com.enginemachining.items.ModdedItemGroups;
import net.minecraft.item.AxeItem;
import net.minecraft.item.IItemTier;

public class AxeCopper extends AxeItem {
    public AxeCopper() {
        super(ModItemTier.copper, 6, -3.2f, new Properties().group(ModdedItemGroups.tools));
        setRegistryName("enginemachining:axe_copper");
    }
}
