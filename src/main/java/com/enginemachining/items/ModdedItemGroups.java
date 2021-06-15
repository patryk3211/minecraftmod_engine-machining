package com.enginemachining.items;

import com.enginemachining.blocks.ModdedBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;

public class ModdedItemGroups {
    public static ItemGroup metals = new ItemGroup("enginemachining.metals") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModdedBlocks.ore_copper.get());
        }

        @Override
        public void fillItemList(NonNullList<ItemStack> items) {
            items.add(new ItemStack(ModdedItems.ingot_copper.get()));
            items.add(new ItemStack(ModdedItems.dust_copper.get()));
            items.add(new ItemStack(ModdedBlocks.ore_copper.get()));
            items.add(new ItemStack(ModdedItems.nugget_copper.get()));

            items.add(new ItemStack(ModdedItems.ingot_tin.get()));
            items.add(new ItemStack(ModdedItems.dust_tin.get()));
            items.add(new ItemStack(ModdedBlocks.ore_tin.get()));
            items.add(new ItemStack(ModdedItems.nugget_tin.get()));

            items.add(new ItemStack(ModdedItems.ingot_aluminium.get()));
            items.add(new ItemStack(ModdedItems.dust_aluminium.get()));
            items.add(new ItemStack(ModdedBlocks.ore_aluminium.get()));
            items.add(new ItemStack(ModdedItems.nugget_aluminium.get()));

            items.add(new ItemStack(ModdedItems.ingot_nickel.get()));
            items.add(new ItemStack(ModdedItems.dust_nickel.get()));
            items.add(new ItemStack(ModdedBlocks.ore_nickel.get()));
            items.add(new ItemStack(ModdedItems.nugget_nickel.get()));

            items.add(new ItemStack(ModdedItems.ingot_lead.get()));
            items.add(new ItemStack(ModdedItems.dust_lead.get()));
            items.add(new ItemStack(ModdedBlocks.ore_lead.get()));
            items.add(new ItemStack(ModdedItems.nugget_lead.get()));

            items.add(new ItemStack(ModdedItems.ingot_silver.get()));
            items.add(new ItemStack(ModdedItems.dust_silver.get()));
            items.add(new ItemStack(ModdedBlocks.ore_silver.get()));
            items.add(new ItemStack(ModdedItems.nugget_silver.get()));

            items.add(new ItemStack(ModdedItems.dust_coal.get()));
            items.add(new ItemStack(ModdedItems.dust_lapis.get()));
            items.add(new ItemStack(ModdedItems.dust_gold.get()));
            items.add(new ItemStack(ModdedItems.dust_iron.get()));

            items.add(new ItemStack(ModdedItems.dust_diamond.get()));

        }
    };

    public static ItemGroup tools = new ItemGroup("enginemachining.tools") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModdedItems.pickaxe_copper.get());
        }

        @Override
        public void fillItemList(NonNullList<ItemStack> items) {
            items.add(new ItemStack(ModdedItems.pickaxe_copper.get()));
            items.add(new ItemStack(ModdedItems.axe_copper.get()));
            items.add(new ItemStack(ModdedItems.sword_copper.get()));
            items.add(new ItemStack(ModdedItems.hoe_copper.get()));
            items.add(new ItemStack(ModdedItems.shovel_copper.get()));
            items.add(new ItemStack(ModdedItems.helmet_copper.get()));
            items.add(new ItemStack(ModdedItems.chestplate_copper.get()));
            items.add(new ItemStack(ModdedItems.leggins_copper.get()));
            items.add(new ItemStack(ModdedItems.boots_copper.get()));
            items.add(new ItemStack(ModdedItems.pickaxe_silver.get()));
            items.add(new ItemStack(ModdedItems.axe_silver.get()));
            items.add(new ItemStack(ModdedItems.sword_silver.get()));
            items.add(new ItemStack(ModdedItems.hoe_silver.get()));
            items.add(new ItemStack(ModdedItems.shovel_silver.get()));
            items.add(new ItemStack(ModdedItems.helmet_silver.get()));
            items.add(new ItemStack(ModdedItems.chestplate_silver.get()));
            items.add(new ItemStack(ModdedItems.leggins_silver.get()));
            items.add(new ItemStack(ModdedItems.boots_silver.get()));
        }
    };

    public static ItemGroup misc = new ItemGroup("enginemachining.misc") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModdedItems.battery_disposable.get());
        }

        @Override
        public void fillItemList(NonNullList<ItemStack> items) {
            ItemStack battery_disposable = new ItemStack(ModdedItems.battery_disposable.get());

            CompoundNBT nbt = battery_disposable.getTag();
            if(nbt == null) nbt = new CompoundNBT();
            CompoundNBT energyTag = new CompoundNBT();
            energyTag.putInt("charge", 26000);
            energyTag.putInt("maxCharge", 26000);
            energyTag.putInt("maxDischargeSpeed", 100);
            nbt.put("energy", energyTag);
            battery_disposable.setTag(nbt);

            items.add(battery_disposable);

            ItemStack battery_creative = new ItemStack(ModdedItems.battery_disposable.get());

            CompoundNBT nbt2 = battery_creative.getTag();
            if(nbt2 == null) nbt2 = new CompoundNBT();
            CompoundNBT energyTag2 = new CompoundNBT();
            energyTag2.putInt("charge", 10000000);
            energyTag2.putInt("maxCharge", 10000000);
            energyTag2.putInt("maxDischargeSpeed", 1000);
            nbt2.put("energy", energyTag2);
            battery_creative.setTag(nbt2);

            items.add(battery_creative);

            items.add(new ItemStack(ModdedBlocks.infinite_power_source.get()));
        }
    };
}
