package com.enginemachining.renderers;

import com.enginemachining.EngineMachiningMod;
import com.enginemachining.capabilities.ModdedCapabilities;
import com.enginemachining.tileentities.KineticGeneratorTile;
import com.enginemachining.tileentities.ModdedTileEntities;
import com.enginemachining.utils.RenderHelper;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KineticGeneratorRenderer extends TileEntityRenderer<KineticGeneratorTile> {
    private static final ResourceLocation MODEL_LOC = new ResourceLocation(EngineMachiningMod.MOD_ID, "kinetic_generator");

    public KineticGeneratorRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(KineticGeneratorTile tile, float partialTicks, MatrixStack stack, IRenderTypeBuffer type, int combinedLight, int combinedOverlay) {
        BlockState state = tile.getBlockState();
        Direction facing = state.getValue(BlockStateProperties.FACING);

        IBakedModel model = Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation(MODEL_LOC, "facing=" + facing + ",model_type=shaft"));

        IVertexBuilder builder = type.getBuffer(RenderType.solid());
        stack.pushPose();

        TileEntity neighbour = tile.getLevel().getBlockEntity(tile.getBlockPosition().offset(facing.getNormal()));
        if(neighbour != null) {
            neighbour.getCapability(ModdedCapabilities.ROTATION, facing.getOpposite()).ifPresent(handler -> {
                double angle = handler.getCurrentAngle();
                Vector3i normal = facing.getOpposite().getNormal();
                Vector3f plane = new Vector3f((1-Math.abs(normal.getX()))*0.5f, (1-Math.abs(normal.getY()))*0.5f, (1-Math.abs(normal.getZ()))*0.5f);
                stack.translate(plane.x(), plane.y(), plane.z());
                stack.mulPose(new Quaternion((float)(normal.getX()*angle), (float)(normal.getY()*angle), (float)(normal.getZ()*angle), true));
                stack.translate(-plane.x(), -plane.y(), -plane.z());
            });
        }

        RenderHelper.renderModel(builder, stack, model, state, combinedLight, combinedOverlay, Minecraft.getInstance().level.random);

        stack.popPose();
    }

    public static void register() {
        ClientRegistry.bindTileEntityRenderer(ModdedTileEntities.kinetic_generator.get(), KineticGeneratorRenderer::new);
    }
}
