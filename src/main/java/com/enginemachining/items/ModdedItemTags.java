package com.enginemachining.items;

import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;

public class ModdedItemTags {
    public static ITag<Item> batteries;

    public static void InitTags() {
        batteries = ItemTags.getAllTags().getTag(new ResourceLocation("enginemachining:batteries"));
    }
}
