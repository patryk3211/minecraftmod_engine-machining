package com.enginemachining.items;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class BatteryDisposable extends Item {
    public BatteryDisposable() {
        super(new Item.Properties()
                .maxStackSize(1)
                .group(ItemGroup.MISC)
                .maxDamage(1000)
                .setNoRepair());
        setRegistryName("enginemachining:battery_disposable");
    }
}
