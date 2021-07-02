package com.enginemachining.renderers;

import com.enginemachining.api.rotation.ClientRotationalNetwork;
import com.enginemachining.api.rotation.KineticBlockProperties;
import com.enginemachining.tileentities.KineticGeneratorTile;
import com.enginemachining.tileentities.ModdedTileEntities;
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
        Direction facing = state.getValue(BlockStateProperties.FACING);

        stack.pushPose();

        ClientRotationalNetwork net = (ClientRotationalNetwork) tile.getNetwork();
        if(net != null) {
            double angle = net.getAngle();
            Vector3i normal = facing.getOpposite().getNormal();
            Vector3f plane = new Vector3f((1 - Math.abs(normal.getX())) * 0.5f, (1 - Math.abs(normal.getY())) * 0.5f, (1 - Math.abs(normal.getZ())) * 0.5f);
            stack.translate(plane.x(), plane.y(), plane.z());
            stack.mulPose(new Quaternion((float) (normal.getX() * angle), (float) (normal.getY() * angle), (float) (normal.getZ() * angle), true));
            stack.translate(-plane.x(), -plane.y(), -plane.z());
        }

        Minecraft.getInstance().getBlockRenderer().renderBlock(state.setValue(KineticBlockProperties.MODEL_TYPE, KineticBlockProperties.ModelType.SHAFT), stack, type, combinedLight, combinedOverlay, EmptyModelData.INSTANCE);

        stack.popPose();
    }

    public static void register() {
        ClientRegistry.bindTileEntityRenderer(ModdedTileEntities.kinetic_generator.get(), KineticGeneratorRenderer::new);
    }
}
