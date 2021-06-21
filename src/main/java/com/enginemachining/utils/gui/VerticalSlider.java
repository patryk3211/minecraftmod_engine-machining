package com.enginemachining.utils.gui;

import com.enginemachining.EngineMachiningMod;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class VerticalSlider extends Widget {
    private static final ResourceLocation WIDGET_TEXTURE = new ResourceLocation(EngineMachiningMod.MOD_ID, "textures/gui/widgets.png");

    public VerticalSlider(int posX, int posY, int width, int height) {
        super(posX, posY, width, height, new StringTextComponent("Slider"));
    }

    /*public void render(MatrixStack stack, int mouseX, int mouseY) {
        mc_instance.getTextureManager().bind(WIDGET_TEXTURE);
        this.blit();
    }*/

    /*@Override
    protected void renderBg(MatrixStack stack, Minecraft mc, int mouseX, int mouseY) {
        mc.getTextureManager().bind(WIDGET_TEXTURE);
        this.blit(stack, x, y, 0, 0, 1, 1);
    }*/
}
