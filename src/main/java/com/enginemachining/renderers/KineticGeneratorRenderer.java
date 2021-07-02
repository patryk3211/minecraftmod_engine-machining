package com.enginemachining.renderers;

import com.enginemachining.api.rotation.ClientRotationalNetwork;
import com.enginemachining.api.rotation.KineticBlockProperties;
import com.enginemachining.tileentities.KineticGeneratorTile;
import com.enginemachining.tileentities.ModdedTileEntities;
import com.enginemachining.utils.RenderHelper;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KineticGeneratorRenderer extends TileEntityRenderer<KineticGeneratorTile> {
    public KineticGeneratorRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(KineticGeneratorTile tile, float partialTicks, MatrixStack stack, IRenderTypeBuffer type, int combinedLight, int combinedOverlay) {
        BlockState state = tile.getBlockState();
        RenderHelper.renderShaft(type, stack, state, (ClientRotationalNetwork) tile.getNetwork(), state.getValue(BlockStateProperties.FACING).getAxis(), combinedLight, combinedOverlay);
    }

    public static void register() {
        ClientRegistry.bindTileEntityRenderer(ModdedTileEntities.kinetic_generator.get(), KineticGeneratorRenderer::new);
    }
}
