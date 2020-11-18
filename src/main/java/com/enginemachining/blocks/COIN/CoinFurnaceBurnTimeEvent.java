package com.enginemachining.blocks.COIN;

import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

import javax.annotation.Nonnull;

@Cancelable
public class CoinFurnaceBurnTimeEvent extends Event
{
    @Nonnull
    private final ItemStack itemStack;
    private int burnTime;

    public CoinFurnaceBurnTimeEvent(@Nonnull ItemStack itemStack, int burnTime)
    {
        this.itemStack = itemStack;
        this.burnTime = burnTime;
    }


    @Nonnull
    public ItemStack getItemStack()
    {
        return itemStack;
    }


    public void setBurnTime(int burnTime)
    {
        if (burnTime >= 0)
        {
            this.burnTime = burnTime;
            setCanceled(true);
        }
    }


    public int getBurnTime()
    {
        return burnTime;
    }
}
