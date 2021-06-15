package com.enginemachining.utils;

import com.enginemachining.capabilities.ModdedCapabilities;
import com.enginemachining.handlers.IEnergyReceiver;
import com.enginemachining.handlers.IEnergySender;
import com.enginemachining.tileentities.EnergyWireTile;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import jdk.nashorn.internal.ir.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.*;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawHighlightEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.FORGE)
public class EnergyNetwork extends PipeNetwork {
    private static final List<EnergyNetwork> networks = new ArrayList<>();
    private static int nextId = 0;

    private final int id;

    private static final boolean NETWORK_VISUALISER_ENABLED = false;
    private static final boolean RECEIVER_PATH_VISUALISER_ENABLED = false;

    private static final class RenderTypes extends RenderState {
        static final RenderType MAIN = RenderType.create("display_font",
                DefaultVertexFormats.POSITION_COLOR, GL11.GL_TRIANGLES,
                256, false, false,
                RenderType.State.builder()
                        .setAlphaState(DEFAULT_ALPHA)
                        .setWriteMaskState(COLOR_DEPTH_WRITE)
                        .setLightmapState(NO_LIGHTMAP)
                        .createCompositeState(false));

        public RenderTypes(String p_i225973_1_, Runnable p_i225973_2_, Runnable p_i225973_3_) {
            super(p_i225973_1_, p_i225973_2_, p_i225973_3_);
        }
    }

    @SubscribeEvent
    public static void tickEvent(TickEvent.ServerTickEvent event) {
        if(event.phase == TickEvent.Phase.END) {
            for (EnergyNetwork network : networks) {
                network.transferPower();
            }
        }
    }

    private static void drawBox(Matrix4f matrix, float r, float g, float b, BufferBuilder builder) {
        builder.vertex(matrix,-0.5f, 0.5f, 0.5f).color(r, g, b, 1.0f).endVertex();
        builder.vertex(matrix,0.5f, -0.5f, 0.5f).color(r, g, b, 1.0f).endVertex();
        builder.vertex(matrix,-0.5f, -0.5f, 0.5f).color(r, g, b, 1.0f).endVertex();
        builder.vertex(matrix,0.5f, -0.5f, 0.5f).color(r, g, b, 1.0f).endVertex();
        builder.vertex(matrix,-0.5f, 0.5f, 0.5f).color(r, g, b, 1.0f).endVertex();
        builder.vertex(matrix,0.5f, 0.5f, 0.5f).color(r, g, b, 1.0f).endVertex();

        builder.vertex(matrix,-0.5f, 0.5f, -0.5f).color(r, g, b, 1.0f).endVertex();
        builder.vertex(matrix,0.5f, -0.5f, -0.5f).color(r, g, b, 1.0f).endVertex();
        builder.vertex(matrix,-0.5f, -0.5f, -0.5f).color(r, g, b, 1.0f).endVertex();
        builder.vertex(matrix,0.5f, -0.5f, -0.5f).color(r, g, b, 1.0f).endVertex();
        builder.vertex(matrix,-0.5f, 0.5f, -0.5f).color(r, g, b, 1.0f).endVertex();
        builder.vertex(matrix,0.5f, 0.5f, -0.5f).color(r, g, b, 1.0f).endVertex();

        builder.vertex(matrix,-0.5f, 0.5f, 0.5f).color(r, g, b, 1.0f).endVertex();
        builder.vertex(matrix,0.5f, 0.5f, -0.5f).color(r, g, b, 1.0f).endVertex();
        builder.vertex(matrix,-0.5f, 0.5f, -0.5f).color(r, g, b, 1.0f).endVertex();
        builder.vertex(matrix,0.5f, 0.5f, -0.5f).color(r, g, b, 1.0f).endVertex();
        builder.vertex(matrix,-0.5f, 0.5f, 0.5f).color(r, g, b, 1.0f).endVertex();
        builder.vertex(matrix,0.5f, 0.5f, 0.5f).color(r, g, b, 1.0f).endVertex();

        builder.vertex(matrix,-0.5f, -0.5f, 0.5f).color(r, g, b, 1.0f).endVertex();
        builder.vertex(matrix,0.5f, -0.5f, -0.5f).color(r, g, b, 1.0f).endVertex();
        builder.vertex(matrix,-0.5f, -0.5f, -0.5f).color(r, g, b, 1.0f).endVertex();
        builder.vertex(matrix,0.5f, -0.5f, -0.5f).color(r, g, b, 1.0f).endVertex();
        builder.vertex(matrix,-0.5f, -0.5f, 0.5f).color(r, g, b, 1.0f).endVertex();
        builder.vertex(matrix,0.5f, -0.5f, 0.5f).color(r, g, b, 1.0f).endVertex();

        builder.vertex(matrix,0.5f, 0.5f, -0.5f).color(r, g, b, 1.0f).endVertex();
        builder.vertex(matrix,0.5f, -0.5f, 0.5f).color(r, g, b, 1.0f).endVertex();
        builder.vertex(matrix,0.5f, -0.5f, -0.5f).color(r, g, b, 1.0f).endVertex();
        builder.vertex(matrix,0.5f, -0.5f, 0.5f).color(r, g, b, 1.0f).endVertex();
        builder.vertex(matrix,0.5f, 0.5f, -0.5f).color(r, g, b, 1.0f).endVertex();
        builder.vertex(matrix,0.5f, 0.5f, 0.5f).color(r, g, b, 1.0f).endVertex();

        builder.vertex(matrix,-0.5f, 0.5f, -0.5f).color(r, g, b, 1.0f).endVertex();
        builder.vertex(matrix,-0.5f, -0.5f, 0.5f).color(r, g, b, 1.0f).endVertex();
        builder.vertex(matrix,-0.5f, -0.5f, -0.5f).color(r, g, b, 1.0f).endVertex();
        builder.vertex(matrix,-0.5f, -0.5f, 0.5f).color(r, g, b, 1.0f).endVertex();
        builder.vertex(matrix,-0.5f, 0.5f, -0.5f).color(r, g, b, 1.0f).endVertex();
        builder.vertex(matrix,-0.5f, 0.5f, 0.5f).color(r, g, b, 1.0f).endVertex();
    }

    // Debug energy network visualiser
    @SubscribeEvent
    public static void renderEvent(RenderWorldLastEvent event) {
        if(NETWORK_VISUALISER_ENABLED) {
            MatrixStack stack = event.getMatrixStack();
            stack.pushPose();

            GlStateManager._disableCull();
            for (int i = 0; i < networks.size(); i++) {
                EnergyNetwork network = networks.get(i);
                Vector3f rgb = new Vector3f((network.id % 8f) / 8f, (network.id / 8f % 8f) / 8f, (network.id / 8f / 8f % 8f) / 8f);
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder builder = tessellator.getBuilder();
                builder.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_COLOR);
                for (BlockPos blockPos : network.pipes.keySet()) {
                    stack.pushPose();

                    Vector3d pp = Minecraft.getInstance().cameraEntity.getEyePosition(0);
                    stack.translate(-(pp.x - blockPos.getX() - 0.5f), -(pp.y - blockPos.getY() - 0.5f), -(pp.z - blockPos.getZ() - 0.5f));

                    stack.scale(0.2f, 0.2f, 0.2f);
                    stack.translate(network.id % 16f / 16f, network.id / 16f % 16f / 16f, 0);

                    drawBox(stack.last().pose(), rgb.x(), rgb.y(), rgb.z(), builder);

                    stack.popPose();
                }

                for (BlockPos blockPos : network.receivers.keySet()) {
                    stack.pushPose();

                    Vector3d pp = Minecraft.getInstance().cameraEntity.getEyePosition(0);
                    stack.translate(-(pp.x - blockPos.getX() - 0.5f), -(pp.y - blockPos.getY() - 0.5f), -(pp.z - blockPos.getZ() - 0.5f));

                    stack.scale(1.1f, 1.1f, 1.1f);
                    //stack.translate(network.id % 16f / 16f, network.id / 16f % 16f / 16f, 0);

                    drawBox(stack.last().pose(), rgb.x(), rgb.y(), rgb.z(), builder);

                    stack.popPose();
                }

                for (BlockPos blockPos : network.senders.keySet()) {
                    stack.pushPose();

                    Vector3d pp = Minecraft.getInstance().cameraEntity.getEyePosition(0);
                    stack.translate(-(pp.x - blockPos.getX() - 0.5f), -(pp.y - blockPos.getY() - 0.5f), -(pp.z - blockPos.getZ() - 0.5f));

                    stack.scale(1.1f, 1.1f, 1.1f);
                    //stack.translate(network.id % 16f / 16f, network.id / 16f % 16f / 16f, 0);

                    drawBox(stack.last().pose(), rgb.x(), rgb.y(), rgb.z(), builder);

                    stack.popPose();
                }

                tessellator.end();
            }

            stack.popPose();

            GlStateManager._enableCull();
        }
    }

    @SubscribeEvent
    public static void onHighlight(DrawHighlightEvent.HighlightBlock event) {
        if(RECEIVER_PATH_VISUALISER_ENABLED) {
            GlStateManager._disableCull();
            GlStateManager._disableTexture();
            MatrixStack stack = event.getMatrix();
            stack.pushPose();
            BlockPos pos = event.getTarget().getBlockPos();
            Vector3d pp = Minecraft.getInstance().cameraEntity.getEyePosition(0);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder builder = tessellator.getBuilder();
            builder.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_COLOR);
            PipeNetwork net = null;
            for (EnergyNetwork network : networks) {
                boolean found = false;
                for (BlockPos blockPos : network.receivers.keySet()) {
                    if(blockPos.getX() == pos.getX() && blockPos.getY() == pos.getY() && blockPos.getZ() == pos.getZ()) {
                        net = network;
                        found = true;
                        break;
                    }
                }
                if(found) break;
            }
            if(net != null) {
                ReceiverPacket rec = net.receivers.get(pos);
                float r = 0;
                float g = 0;
                float b = 0;
                for (ReceiverToSenderPathList pathList : rec.pathsList.values()) {
                    b++;
                    for (ReceiverToSenderPathListEntry path : pathList.paths) {
                        IPipeTraceable prev = null;
                        for (IPipeTraceable pipe : path.pipes) {
                            //g++;
                            stack.pushPose();
                            stack.translate(-(pp.x - pipe.getBlockPos().getX() - 0.5f), -(pp.y - pipe.getBlockPos().getY() - 0.5f), -(pp.z - pipe.getBlockPos().getZ() - 0.5f));
                            stack.translate(r/10f, r/10f, 0.2f);
                            stack.scale(0.1f, 0.1f, 0.1f);
                            if(prev != null) {
                                BlockPos dP = pipe.getBlockPos().subtract(prev.getBlockPos());
                                stack.scale(Math.abs(dP.getX())*3f+1f, Math.abs(dP.getY())*3f+1f, Math.abs(dP.getZ())*3f+1f);
                                stack.translate(-dP.getX()/2f, -dP.getY()/2f, -dP.getZ()/2f);
                            }
                            drawBox(stack.last().pose(), r/4f, g/32f, b/16f, builder);
                            stack.popPose();
                            prev = pipe;
                        }
                        r++;
                    }
                }
            }
            tessellator.end();
            stack.popPose();
            GlStateManager._enableTexture();
            GlStateManager._enableCull();
        }
    }

    @SubscribeEvent
    public static void onWorldUnload(WorldEvent.Unload event) {
        nextId = 0;
        networks.clear();
    }

    public EnergyNetwork(World level) {
        super(level, ModdedCapabilities.ENERGY);

        id = nextId;
        nextId++;

        networks.add(this);
    }

    @Override
    public void delete() {
        networks.removeIf(energyNetwork -> energyNetwork.id == id);
        super.delete();
    }

    public void transferPower() {
        int sendersCount = senders.size();
        int receiversCount = receivers.size();
        if(receiversCount == 0 || sendersCount == 0) return;
        AtomicReference<Float> requestedTransfer = new AtomicReference<>((float) 0);
        AtomicReference<Float> maxAvailableTransfer = new AtomicReference<>((float) 0);
        Map<IEnergySender, Float> senderList = new HashMap<>();
        Map<IEnergyReceiver, Float> receiverList = new HashMap<>();
        senders.forEach((pos, pair) -> {
            if(pair instanceof IEnergySender) {
                IEnergySender sender = (IEnergySender) pair;
                float max_extract = sender.getHandler().extractPower(Integer.MAX_VALUE, true);
                maxAvailableTransfer.updateAndGet(v -> v + max_extract);
                senderList.put(sender, 0f);
            }
        });
        receivers.forEach((pos, rec) -> {
            if(rec.receiver instanceof IEnergyReceiver) {
                IEnergyReceiver receiver = (IEnergyReceiver) rec.receiver;
                float max_insert = receiver.getHandler().insertPower(Integer.MAX_VALUE, true);
                requestedTransfer.updateAndGet(v -> v + max_insert);
                receiverList.put(receiver, 0f);

            }
        });
        float powerToTransfer = Float.min(requestedTransfer.get(), maxAvailableTransfer.get());

        float left = powerToTransfer;
        int count = sendersCount;

        // Extract the given amount of power from each sender.
        for(IEnergySender sender : senderList.keySet()) {
            float max_transfer = sender.getHandler().extractPower(left/(float) count, false);
            left -= max_transfer;
            count--;
            senderList.replace(sender, max_transfer);
        }

        left = powerToTransfer;
        count = receiversCount;

        // Insert the given amount of power into every receiver.
        for(IEnergyReceiver receiver : receiverList.keySet()) {
            float max_transfer = receiver.getHandler().insertPower(left/(float) count, false);
            left -= max_transfer;
            count--;
            receiverList.replace(receiver, max_transfer);
        }

        // Add power flow to each pipe in each path.
        receivers.forEach((pos, packet) -> {
            IPipeTraceable traceable = packet.receiver;
            if(traceable instanceof IEnergyReceiver) {
                IEnergyReceiver receiver = (IEnergyReceiver) traceable;
                final float powerToReceive = receiverList.get(receiver);
                final float powerPerSender = powerToReceive / packet.pathsList.size();
                packet.pathsList.forEach((sender, pathList) -> {
                    for (ReceiverToSenderPathListEntry pathEntry : pathList.paths) {
                        final float powerPerWire;
                        if(pathList.combinedResistance == 0) powerPerWire = powerPerSender/pathList.paths.size();
                        else powerPerWire = (powerPerSender / pathList.combinedResistance) * pathEntry.pathResistance;
                        for (IPipeTraceable pipe : pathEntry.pipes) {
                            if(pipe instanceof EnergyWireTile) ((EnergyWireTile) pipe).addPowerFlow(powerPerWire);
                        }
                    }
                });
            }
        });
    }
}
