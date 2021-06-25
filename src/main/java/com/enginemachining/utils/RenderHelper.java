package com.enginemachining.utils;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.data.ModelDataMap;

import java.util.List;
import java.util.Random;

public class RenderHelper {
    public static void renderModel(IVertexBuilder builder, MatrixStack matrixStack, IBakedModel model, BlockState state, int light, int overlay, Random randomIn) {
        // Render the culled faces
        for(Direction dir : Direction.values()) {
            List<BakedQuad> quads = model.getQuads(state, dir, randomIn, new ModelDataMap.Builder().build());
            for (BakedQuad quad : quads) {
                builder.addVertexData(matrixStack.last(), quad, 1, 1, 1, 1, light, overlay);
            }
        }
        // Render the unculled faces
        List<BakedQuad> quads = model.getQuads(state, null, randomIn, new ModelDataMap.Builder().build());
        for (BakedQuad quad : quads) {
            builder.addVertexData(matrixStack.last(), quad, 1, 1,1, 1, light, overlay);
        }
    }
}
