package com.enginemachining.screens;

import com.enginemachining.EngineMachiningMod;
import com.enginemachining.EngineMachiningPacketHandler;
import com.enginemachining.containers.PowerLimiterContainer;
import com.enginemachining.messages.CrusherTileMessage;
import com.enginemachining.messages.PowerLimiterMessage;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class PowerLimiterScreen extends ContainerScreen<PowerLimiterContainer> implements IHasContainer<PowerLimiterContainer> {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(EngineMachiningMod.MOD_ID, "textures/gui/power_limiter.png");

    private int xOrigin;
    private int yOrigin;

    public PowerLimiterScreen(PowerLimiterContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);

        leftPos = 0;
        topPos = 0;
        imageHeight = 165;
        imageWidth = 175;

        xOrigin = 0;
        yOrigin = 0;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(button == 0) {
            int onButtonOriginX = xOrigin + 80;
            int onButtonOriginY = yOrigin + 32;
            int offButtonOriginX = xOrigin + 80;
            int offButtonOriginY = yOrigin + 46;
            int buttonWidth = 17;
            int buttonHeight = 13;
            if (mouseX > onButtonOriginX && mouseX < onButtonOriginX + buttonWidth && mouseY > onButtonOriginY && mouseY < onButtonOriginY + buttonHeight) {
                if(menu.trackedArray.get(4) == 0) EngineMachiningPacketHandler.INSTANCE.sendToServer(new PowerLimiterMessage(menu.tileEntity.getBlockPos(), true));
            }
            if (mouseX > offButtonOriginX && mouseX < offButtonOriginX + buttonWidth && mouseY > offButtonOriginY && mouseY < offButtonOriginY + buttonHeight) {
                if(menu.trackedArray.get(4) == 1) EngineMachiningPacketHandler.INSTANCE.sendToServer(new PowerLimiterMessage(menu.tileEntity.getBlockPos(), false));
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void renderBg(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        minecraft.getTextureManager().bind(GUI_TEXTURE);

        xOrigin = (this.width - this.imageWidth) / 2;
        yOrigin = (this.height - this.imageHeight) / 2;
        this.blit(stack, xOrigin, yOrigin, 0, 0, this.imageWidth, this.imageHeight);

        float bufferBarRatio = (float)menu.trackedArray.get(0)/menu.trackedArray.get(1);
        int energyBarHeight = (int)(bufferBarRatio * 68);
        this.blit(stack, xOrigin+65, yOrigin+(68-energyBarHeight)+7, 209, 1, 5, energyBarHeight);

        float extractBarRatio = (float)menu.trackedArray.get(2)/menu.trackedArray.get(3);
        int extractBarHeight = (int)(extractBarRatio * 68);
        this.blit(stack, xOrigin+104, yOrigin+(68-extractBarHeight)+7, 216, 1, 5, extractBarHeight);

        if(menu.trackedArray.get(4) == 1) {
            this.blit(stack, xOrigin+80, yOrigin+32, 194, 0, 14, 27);
        }
    }
}
