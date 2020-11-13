package com.enginemachining.items;

import com.enginemachining.blocks.ModdedBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ModdedItemGroups {
    public static ItemGroup ores = new ItemGroup("itemgroup.enginemachining.ores") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModdedBlocks.ore_copper);
        }

        @Override
        public void fill(NonNullList<ItemStack> items) {
            items.add(new ItemStack(ModdedBlocks.ore_copper));
            items.add(new ItemStack(ModdedBlocks.ore_tin));
            items.add(new ItemStack(ModdedBlocks.ore_aluminium));
            items.add(new ItemStack(ModdedBlocks.ore_nickel));
            items.add(new ItemStack(ModdedBlocks.ore_lead));
            items.add(new ItemStack(ModdedBlocks.ore_silver));
            //super.fill(items);
        }
    };
}
