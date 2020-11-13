package com.enginemachining.items.tools;

import com.enginemachining.items.ModdedItemGroups;
import com.enginemachining.tools.ModItemTier;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.PickaxeItem;

public class PickaxeCopper extends PickaxeItem {

    public PickaxeCopper(int attackDamageIn, float attackSpeedIn, Properties builder) {
        super(ModItemTier.copper, attackDamageIn, attackSpeedIn, new Properties().group(ItemGroup.TOOLS));
    }
}
