package com.enginemachining.tools;

import com.enginemachining.items.ModdedItemGroups;
import com.enginemachining.items.ModdedItems;
import com.enginemachining.tools.ModItemTier;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.PickaxeItem;

public class PickaxeCopper extends PickaxeItem {
    public PickaxeCopper() {
        super(ModItemTier.copper, 1, 1.0f, new Properties().group(ModdedItemGroups.tools));
        setRegistryName("enginemachining:pickaxe_copper");
    }
}
