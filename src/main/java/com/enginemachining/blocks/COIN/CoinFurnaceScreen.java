package com.enginemachining.blocks.COIN;

import net.minecraft.client.gui.recipebook.FurnaceRecipeGui;
import net.minecraft.client.gui.screen.inventory.AbstractFurnaceScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.FurnaceContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CoinFurnaceScreen extends AbstractFurnaceScreen<FurnaceContainer> {
    private static final ResourceLocation  tu lokacja= new ResourceLocation("tu texture");

    public CoinFurnaceScreen(FurnaceContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, new FurnaceRecipeGui(), playerInventory, title,tu lokacja );
    }
}
