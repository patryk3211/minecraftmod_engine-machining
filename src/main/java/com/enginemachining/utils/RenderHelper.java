package com.enginemachining.utils;

import com.enginemachining.api.rotation.ClientRotationalNetwork;
import com.enginemachining.api.rotation.KineticBlockProperties;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.ModelDataMap;

import java.util.List;
import java.util.Random;

public class RenderHelper {
    private static final Vector3i X_AXIS = new Vector3i(1, 0, 0);
    private static final Vector3i Y_AXIS = new Vector3i(0, 1, 0);
    private static final Vector3i Z_AXIS = new Vector3i(0, 0, 1);

    public static void renderShaft(IRenderTypeBuffer typeBuffer, MatrixStack matrixStack, BlockState state, ClientRotationalNetwork net, Direction.Axis rotationalAxis, int light, int overlay) {
        matrixStack.pushPose();

        if(net != null) {
            double angle = net.getAngle();
            Vector3i normal;
            switch (rotationalAxis) {
                case X: normal = X_AXIS; break;
                case Y: normal = Y_AXIS; break;
                case Z: normal = Z_AXIS; break;
                default: normal = Vector3i.ZERO; break;
            }
            Vector3f plane = new Vector3f((1 - Math.abs(normal.getX())) * 0.5f, (1 - Math.abs(normal.getY())) * 0.5f, (1 - Math.abs(normal.getZ())) * 0.5f);
            matrixStack.translate(plane.x(), plane.y(), plane.z());
            matrixStack.mulPose(new Quaternion((float) (normal.getX() * angle), (float) (normal.getY() * angle), (float) (normal.getZ() * angle), true));
            matrixStack.translate(-plane.x(), -plane.y(), -plane.z());
        }

        Minecraft.getInstance().getBlockRenderer().renderBlock(state.setValue(KineticBlockProperties.MODEL_TYPE, KineticBlockProperties.ModelType.SHAFT), matrixStack, typeBuffer, light, overlay, EmptyModelData.INSTANCE);

        matrixStack.popPose();
    }
}
