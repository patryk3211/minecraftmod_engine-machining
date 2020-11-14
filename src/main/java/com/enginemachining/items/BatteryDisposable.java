package com.enginemachining.items;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class BatteryDisposable extends Item {
    public BatteryDisposable() {
        super(new Item.Properties()
                .maxStackSize(1)
                .group(ItemGroup.MISC));
        setRegistryName("enginemachining:battery_disposable");
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn) {
        CompoundNBT nbt = stack.getTag();
        CompoundNBT energyTag = new CompoundNBT();
        energyTag.putInt("charge", 1000);
        energyTag.putInt("maxCharge", 1000);
        energyTag.putInt("maxDischargeSpeed", 10);
        nbt.put("energy", energyTag);
        stack.setTag(nbt);
    }
}
