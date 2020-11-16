package com.enginemachining.items;

import com.enginemachining.blocks.ModdedBlocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.StringTextComponent;

public class ModdedItemGroups {
    public static ItemGroup metals;

    public static ItemGroup tools;

    public static ItemGroup misc;

    public static void InitItemGroups() {
        metals = new ItemGroup("enginemachining.metals") {
            @Override
            public ItemStack createIcon() {
                return new ItemStack(ModdedBlocks.ore_copper);
            }

            @Override
            public void fill(NonNullList<ItemStack> items) {
                items.add(new ItemStack(ModdedItems.ingot_copper));
                items.add(new ItemStack(ModdedItems.dust_copper));
                items.add(new ItemStack(ModdedBlocks.ore_copper));
                items.add(new ItemStack(ModdedItems.nugget_copper));

                items.add(new ItemStack(ModdedItems.ingot_tin));
                items.add(new ItemStack(ModdedItems.dust_tin));
                items.add(new ItemStack(ModdedBlocks.ore_tin));
                items.add(new ItemStack(ModdedItems.nugget_tin));

                items.add(new ItemStack(ModdedItems.ingot_aluminium));
                items.add(new ItemStack(ModdedItems.dust_aluminium));
                items.add(new ItemStack(ModdedBlocks.ore_aluminium));
                items.add(new ItemStack(ModdedItems.nugget_aluminium));

                items.add(new ItemStack(ModdedItems.ingot_nickel));
                items.add(new ItemStack(ModdedItems.dust_nickel));
                items.add(new ItemStack(ModdedBlocks.ore_nickel));
                items.add(new ItemStack(ModdedItems.nugget_nickel));

                items.add(new ItemStack(ModdedItems.ingot_lead));
                items.add(new ItemStack(ModdedItems.dust_lead));
                items.add(new ItemStack(ModdedBlocks.ore_lead));
                items.add(new ItemStack(ModdedItems.nugget_lead));

                items.add(new ItemStack(ModdedItems.ingot_silver));
                items.add(new ItemStack(ModdedItems.dust_silver));
                items.add(new ItemStack(ModdedBlocks.ore_silver));
                items.add(new ItemStack(ModdedItems.nugget_silver));

            }
        };

        tools = new ItemGroup("enginemachining.tools") {
            @Override
            public ItemStack createIcon() {
                return new ItemStack(ModdedItems.pickaxe_copper);
            }

            @Override
            public void fill(NonNullList<ItemStack> items) {
                items.add(new ItemStack(ModdedItems.pickaxe_copper));
                items.add(new ItemStack(ModdedItems.axe_copper));
                items.add(new ItemStack(ModdedItems.sword_copper));
                items.add(new ItemStack(ModdedItems.hoe_copper));
                items.add(new ItemStack(ModdedItems.shovel_copper));
                items.add(new ItemStack(ModdedItems.helmet_copper));
                items.add(new ItemStack(ModdedItems.chestplate_copper));
                items.add(new ItemStack(ModdedItems.leggins_copper));
                items.add(new ItemStack(ModdedItems.boots_copper));
                items.add(new ItemStack(ModdedItems.pickaxe_silver));
                items.add(new ItemStack(ModdedItems.axe_silver));
                items.add(new ItemStack(ModdedItems.sword_silver));
                items.add(new ItemStack(ModdedItems.hoe_silver));
                items.add(new ItemStack(ModdedItems.shovel_silver));
            }
        };

        misc = new ItemGroup("enginemachining.misc") {
            @Override
            public ItemStack createIcon() {
                return new ItemStack(ModdedItems.battery_disposable);
            }

            @Override
            public void fill(NonNullList<ItemStack> items) {
                ItemStack battery_disposable = new ItemStack(ModdedItems.battery_disposable);

                CompoundNBT nbt = battery_disposable.getTag();
                if(nbt == null) nbt = new CompoundNBT();
                CompoundNBT energyTag = new CompoundNBT();
                energyTag.putInt("charge", 1000);
                energyTag.putInt("maxCharge", 1000);
                energyTag.putInt("maxDischargeSpeed", 10);
                nbt.put("energy", energyTag);
                battery_disposable.setTag(nbt);

                items.add(battery_disposable);
            }
        };
    }
}
