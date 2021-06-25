package com.enginemachining.tools;

import com.enginemachining.items.ModdedItemGroups;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Hammer extends Item {
    public Hammer() {
        super(new Item.Properties()
                .tab(ModdedItemGroups.tools)
                .stacksTo(1)
                .durability(75));
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        ItemStack copy = itemStack.copy();
        copy.setDamageValue(copy.getDamageValue()+1);
        if(copy.getDamageValue() >= 75) copy = ItemStack.EMPTY;
        return copy;
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }
}
