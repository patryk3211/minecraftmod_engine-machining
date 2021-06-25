package com.enginemachining.renderers;

import com.enginemachining.EngineMachiningMod;
import com.enginemachining.tileentities.HandCrankTile;
import com.enginemachining.tileentities.ModdedTileEntities;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.data.BlockModelFields;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Rotations;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.client.model.QuadTransformer;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import javax.annotation.Nullable;
import java.util.List;

public class HandCrankRenderer extends TileEntityRenderer<HandCrankTile> {
    private static final ResourceLocation MODEL_LOC = new ResourceLocation(EngineMachiningMod.MOD_ID, "handcrank");

    public HandCrankRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(HandCrankTile tile, float partialTicks, MatrixStack stack, IRenderTypeBuffer type, int combinedLight, int combinedOverlay) {
        BlockState state = tile.getBlockState();
        Direction facing = state.getValue(BlockStateProperties.FACING);
        // Get the model.
        IBakedModel model = Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation(MODEL_LOC, "facing=" + facing));

        IVertexBuilder builder = type.getBuffer(RenderType.solid());
        stack.pushPose();

        Vector3i normal = facing.getNormal();
        Vector3f plane = new Vector3f((1-Math.abs(normal.getX()))*0.5f, (1-Math.abs(normal.getY()))*0.5f, (1-Math.abs(normal.getZ()))*0.5f);
        stack.translate(plane.x(), plane.y(), plane.z());
        stack.mulPose(new Quaternion((float)(normal.getX()*tile.angle), (float)(normal.getY()*tile.angle), (float)(normal.getZ()*tile.angle), true));
        stack.translate(-plane.x(), -plane.y(), -plane.z());

        // Render the culled faces
        for(Direction dir : Direction.values()) {
            List<BakedQuad> quads = model.getQuads(tile.getBlockState(), dir, Minecraft.getInstance().level.getRandom(), new ModelDataMap.Builder().build());
            for (BakedQuad quad : quads) {
                builder.addVertexData(stack.last(), quad, 1, 1, 1, 1, combinedLight, combinedOverlay);
            }
        }
        // Render the unculled faces
        List<BakedQuad> quads = model.getQuads(tile.getBlockState(), null, Minecraft.getInstance().level.getRandom(), new ModelDataMap.Builder().build());
        for (BakedQuad quad : quads) {
            builder.addVertexData(stack.last(), quad, 1, 1,1, 1, combinedLight, combinedOverlay);
        }
        stack.popPose();

        double rotateBy = tile.toRotate * partialTicks;
        tile.toRotate -= rotateBy;
        tile.angle += rotateBy;
    }

    public static void register() {
        ClientRegistry.bindTileEntityRenderer(ModdedTileEntities.handcrank.get(), HandCrankRenderer::new);
    }
}
