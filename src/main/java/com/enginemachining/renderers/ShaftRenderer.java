package com.enginemachining.renderers;

import com.enginemachining.api.rotation.ClientRotationalNetwork;
import com.enginemachining.tileentities.ModdedTileEntities;
import com.enginemachining.tileentities.ShaftTile;
import com.enginemachining.utils.RenderHelper;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ShaftRenderer extends TileEntityRenderer<ShaftTile> {
    public ShaftRenderer(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);
    }

    @Override
    public void render(ShaftTile tile, float partialTicks, MatrixStack stack, IRenderTypeBuffer type, int combinedLight, int combinedOverlay) {
        BlockState state = tile.getBlockState();
        RenderHelper.renderShaft(type, stack, state, (ClientRotationalNetwork) tile.getNetwork(), state.getValue(BlockStateProperties.AXIS), combinedLight, combinedOverlay);
    }

    public static void register() {
        ClientRegistry.bindTileEntityRenderer(ModdedTileEntities.shaft.get(), ShaftRenderer::new);
    }
}
