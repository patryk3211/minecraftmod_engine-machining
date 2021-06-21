package com.enginemachining.screens;

import com.enginemachining.EngineMachiningPacketHandler;
import com.enginemachining.containers.CrusherContainer;
import com.enginemachining.containers.ModdedContainers;
import com.enginemachining.messages.CrusherTileMessage;
import com.enginemachining.tileentities.CrusherTile;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.PacketDistributor;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.CallbackI;

@OnlyIn(Dist.CLIENT)
public class CrusherScreen extends ContainerScreen<CrusherContainer> implements IHasContainer<CrusherContainer> {
    public static final ResourceLocation textureLocation = new ResourceLocation("enginemachining:textures/gui/crusher.png");

    int xOrigin;
    int yOrigin;

    public CrusherScreen(CrusherContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);

        leftPos = 0;
        topPos = 0;
        imageWidth = 175;
        imageHeight = 165;

        xOrigin = (this.width - this.imageWidth) / 2;
        yOrigin = (this.height - this.imageHeight) / 2;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(button == 0) {
            int onButtonOriginX = xOrigin + 14;
            int onButtonOriginY = yOrigin + 32;
            int offButtonOriginX = xOrigin + 14;
            int offButtonOriginY = yOrigin + 42;
            int buttonWidth = 17;
            int buttonHeight = 9;
            if (mouseX > onButtonOriginX && mouseX < onButtonOriginX + buttonWidth && mouseY > onButtonOriginY && mouseY < onButtonOriginY + buttonHeight) {
                if(menu.trackedArray.get(2) == 0) EngineMachiningPacketHandler.INSTANCE.sendToServer(new CrusherTileMessage(menu.tileEntity.getBlockPos(), true));
            }
            if (mouseX > offButtonOriginX && mouseX < offButtonOriginX + buttonWidth && mouseY > offButtonOriginY && mouseY < offButtonOriginY + buttonHeight) {
                if(menu.trackedArray.get(2) == 1) EngineMachiningPacketHandler.INSTANCE.sendToServer(new CrusherTileMessage(menu.tileEntity.getBlockPos(), false));
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        minecraft.getTextureManager().bind(textureLocation);
        xOrigin = (this.width - this.imageWidth) / 2;
        yOrigin = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, xOrigin, yOrigin, 0, 0, this.imageWidth, this.imageHeight);

        float ratioEnergyBar = (float)menu.trackedArray.get(0) / (float)menu.trackedArray.get(1);
        int barHeight = (int)(ratioEnergyBar * 69);
        this.blit(matrixStack, xOrigin+164, yOrigin+8+(69-barHeight), 176, 69 - barHeight, 5, barHeight);

        if(menu.trackedArray.get(2) == 1) {
            this.blit(matrixStack, xOrigin+13, yOrigin+31, 181, 49, 19, 21);
        }

        float ratioPowerBar = (float)menu.trackedArray.get(3) / (float)CrusherTile.HEAT_MAX;
        int powerBarHeight = (int)(ratioPowerBar * 11);
        this.blit(matrixStack, xOrigin+47, yOrigin+53+(11-powerBarHeight), 176, 79 + (11 - powerBarHeight), 11, powerBarHeight);

        float ratioProgressArrow = (float)menu.trackedArray.get(4) / (float)menu.trackedArray.get(5);
        int ratioBarWidth = (int)(ratioProgressArrow * 39);
        this.blit(matrixStack, xOrigin+68, yOrigin+37, 176, 70, ratioBarWidth, 9);
    }
}
