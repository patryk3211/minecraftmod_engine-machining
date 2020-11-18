package com.enginemachining.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BatteryDisposable extends Item {
    public BatteryDisposable() {
        super(new Item.Properties()
                .maxStackSize(1)
                .group(ModdedItemGroups.misc));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CompoundNBT nbt = stack.getTag();
        if(nbt == null) return;
        CompoundNBT energyTag = nbt.getCompound("energy");
        int charge = energyTag.getInt("charge");
        int maxCharge = energyTag.getInt("maxCharge");
        tooltip.add(new StringTextComponent(charge + "/" + maxCharge + "FE"));
    }
}
