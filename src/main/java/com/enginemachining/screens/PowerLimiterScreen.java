package com.enginemachining.screens;

import com.enginemachining.EngineMachiningMod;
import com.enginemachining.EngineMachiningPacketHandler;
import com.enginemachining.containers.PowerLimiterContainer;
import com.enginemachining.messages.CrusherTileMessage;
import com.enginemachining.messages.PowerLimiterMessage;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MouseHelper;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.AbstractSlider;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;
import net.minecraftforge.fml.client.gui.widget.Slider;

import java.util.ArrayList;
import java.util.List;

public class PowerLimiterScreen extends ContainerScreen<PowerLimiterContainer> implements IHasContainer<PowerLimiterContainer> {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(EngineMachiningMod.MOD_ID, "textures/gui/power_limiter.png");

    private int xOrigin;
    private int yOrigin;

    private double sliderPos;
    private boolean sliderStuck;

    private float animationStage;
    private float animationStage2;

    public PowerLimiterScreen(PowerLimiterContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);

        leftPos = 0;
        topPos = 0;
        imageHeight = 181;
        imageWidth = 176;

        inventoryLabelY += 15;

        xOrigin = 0;
        yOrigin = 0;

        sliderStuck = false;

        sliderPos = (double)menu.trackedArray.get(5)/menu.trackedArray.get(3);

        animationStage = 0;
        animationStage2 = 0;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(button == 0) {
            int onButtonOriginX = xOrigin + 80;
            int onButtonOriginY = yOrigin + 32+10;
            int offButtonOriginX = xOrigin + 80;
            int offButtonOriginY = yOrigin + 46+10;
            int buttonWidth = 17;
            int buttonHeight = 13;
            if (mouseX > onButtonOriginX && mouseX < onButtonOriginX + buttonWidth && mouseY > onButtonOriginY && mouseY < onButtonOriginY + buttonHeight) {
                if(menu.trackedArray.get(4) == 0) EngineMachiningPacketHandler.INSTANCE.sendToServer(new PowerLimiterMessage(menu.tileEntity.getBlockPos(), true, -1));
            }
            if (mouseX > offButtonOriginX && mouseX < offButtonOriginX + buttonWidth && mouseY > offButtonOriginY && mouseY < offButtonOriginY + buttonHeight) {
                if(menu.trackedArray.get(4) == 1) EngineMachiningPacketHandler.INSTANCE.sendToServer(new PowerLimiterMessage(menu.tileEntity.getBlockPos(), false, -1));
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double p_231045_6_, double p_231045_8_) {
        // Stick slider to mouse cursor
        double sliderOffset = (sliderPos*68f/(68f/64f));
        if(mouseX >= xOrigin+112 && mouseY > yOrigin+71-sliderOffset+10 && mouseX <= xOrigin+112+7 && mouseY <= yOrigin+71+4-sliderOffset+10 && button == 0) sliderStuck = true;
        if(sliderStuck) {
            // Move slider to cursor
            if(mouseY >= yOrigin+7+2+10 && mouseY <= yOrigin+74+10) {
                sliderPos = 1.0 - (mouseY - (yOrigin+7+10) - 2)/65.0;
            }
        }
        return super.mouseDragged(mouseX, mouseY, button, p_231045_6_, p_231045_8_);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        // Unstick slider
        sliderStuck = false;
        EngineMachiningPacketHandler.INSTANCE.sendToServer(new PowerLimiterMessage(menu.tileEntity.getBlockPos(), menu.trackedArray.get(4) == 1, (int)(sliderPos*menu.trackedArray.get(3))));
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    protected void renderBg(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        minecraft.getTextureManager().bind(GUI_TEXTURE);

        if(Double.compare(sliderPos, Double.NaN) == 0) sliderPos = (double)menu.trackedArray.get(5)/menu.trackedArray.get(3);

        xOrigin = (this.width - this.imageWidth) / 2;
        yOrigin = (this.height - this.imageHeight) / 2;
        this.blit(stack, xOrigin, yOrigin, 0, 0, this.imageWidth, this.imageHeight);

        float bufferBarRatio = (float)menu.trackedArray.get(0)/menu.trackedArray.get(1);
        int energyBarHeight = (int)(bufferBarRatio * 68);
        this.blit(stack, xOrigin+65, yOrigin+(68-energyBarHeight)+7+10, 209, 1, 5, energyBarHeight);

        float extractBarRatio = (float)menu.trackedArray.get(2)/menu.trackedArray.get(3);
        int extractBarHeight = (int)(extractBarRatio * 68);
        this.blit(stack, xOrigin+104, yOrigin+(68-extractBarHeight)+7+10, 216, 1+(68-extractBarHeight), 5, extractBarHeight);

        if(menu.trackedArray.get(4) == 1) this.blit(stack, xOrigin+80, yOrigin+32+10, 194, 0, 14, 28);

        // Slider
        double sliderOffset = (sliderPos*68f/(68f/64f));
        if(mouseX >= xOrigin+112 && mouseY > yOrigin+71-sliderOffset+10 && mouseX <= xOrigin+112+7 && mouseY <= yOrigin+71+4-sliderOffset+10) this.blit(stack, xOrigin+112, yOrigin+71-(int)sliderOffset+10, 183, 30, 7, 4);
        else this.blit(stack, xOrigin+112, yOrigin+71-(int)sliderOffset+10, 176, 30, 7, 4);

        List<ITextComponent> lines = new ArrayList<>();
        if(mouseX >= xOrigin+64 && mouseY >= yOrigin+6+10 && mouseX <= xOrigin+70 && mouseY <= yOrigin+75+10) {
            lines.add(new StringTextComponent("Internal Buffer:"));
            lines.add(new StringTextComponent(menu.trackedArray.get(0) + "/" + menu.trackedArray.get(1)));
        }
        if(mouseX >= xOrigin+103 && mouseY >= yOrigin+6+10 && mouseX <= xOrigin+109 && mouseY <= yOrigin+75+10) {
            lines.add(new StringTextComponent("Current Transfer:"));
            lines.add(new StringTextComponent(menu.trackedArray.get(2) + " EE/t"));
        }
        if(mouseX >= xOrigin+111 && mouseY >= yOrigin+6+10 && mouseX <= xOrigin+119 && mouseY <= yOrigin+75+10) {
            lines.add(new StringTextComponent("Max Transfer:"));
            if(!sliderStuck) lines.add(new StringTextComponent(menu.trackedArray.get(5) + " EE/t"));
            else lines.add(new StringTextComponent((int)(sliderPos*menu.trackedArray.get(3)) + " EE/t"));
        }

        if(menu.trackedArray.get(0) > 0 || menu.trackedArray.get(2) > 0) {
            this.blit(stack, xOrigin+72, yOrigin+45+10, 176, 0, 9, 30);
            if(menu.trackedArray.get(4) == 1) {
                this.blit(stack, xOrigin+93, yOrigin+50+10, 185, 0, 9, 25);
            }
        }

        if(menu.trackedArray.get(2) > 0) {
            animationStage += partialTicks;
            animationStage = animationStage % 4;
            double multiplier = (double) menu.trackedArray.get(2)/menu.trackedArray.get(3);
            if(multiplier < 0.1) multiplier = 0.1;
            animationStage2 += partialTicks * multiplier;
            animationStage2 = animationStage2 % 4;
        } else {
            animationStage = 0;
            animationStage2 = 0;
        }

        this.blit(stack, xOrigin+73, yOrigin+65+(3-(int)animationStage)+10, 190, 30+(3-(int)animationStage), 9, (int)animationStage);
        this.blit(stack, xOrigin+93, yOrigin+65+10, 199, 30, 9, (int)animationStage2);

        if(lines.size() != 0) {
            GuiUtils.preItemToolTip(ItemStack.EMPTY);
            GuiUtils.drawHoveringText(stack, lines, mouseX, mouseY, width, height, -1, font);
            GuiUtils.postItemToolTip();
        }
    }
}
