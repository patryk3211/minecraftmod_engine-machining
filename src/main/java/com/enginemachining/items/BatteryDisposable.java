package com.enginemachining.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BatteryDisposable extends Item {
    public BatteryDisposable() {
        super(new Item.Properties()
                .stacksTo(1)
                .tab(ModdedItemGroups.misc));
    }



    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CompoundNBT nbt = stack.getTag();
        if(nbt == null || nbt.getCompound("energy").isEmpty()) {
            tooltip.add(new StringTextComponent("Invalid tag"));
            return;
        }
        CompoundNBT energyTag = nbt.getCompound("energy");
        int charge = energyTag.getInt("charge");
        int maxCharge = energyTag.getInt("maxCharge");
        tooltip.add(new StringTextComponent(charge + "/" + maxCharge + "FE"));
    }
}
