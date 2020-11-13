package com.enginemachining.items;

import com.enginemachining.blocks.ModdedBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ModdedItemGroups {
    public static ItemGroup metals = new ItemGroup("enginemachining.metals") {
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
            items.add(new ItemStack(ModdedItems.ingot_tin));
            items.add(new ItemStack(ModdedItems.ingot_nickel));
            items.add(new ItemStack(ModdedItems.ingot_copper));
            items.add(new ItemStack(ModdedItems.ingot_aluminium));
            items.add(new ItemStack(ModdedItems.ingot_lead));
            items.add(new ItemStack(ModdedItems.ingot_silver));
            //super.fill(items);
        }
    };

    /*public static ItemGroup tools = new ItemGroup("enginemachining.tools") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModdedItems.pickaxe_copper);
        }

        @Override
        public void fill(NonNullList<ItemStack> items) {
            super.fill(items);
        }
    };*/
}
