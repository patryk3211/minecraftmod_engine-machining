package com.enginemachining.api.rotation;

import com.enginemachining.capabilities.ModdedCapabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;

import java.util.*;

@OnlyIn(Dist.CLIENT)
public class ClientRotationalNetwork implements IRotationalNetwork {
    private static final Map<UUID, ClientRotationalNetwork> networks = new HashMap<>();

    private float speed;
    private final Map<BlockPos, LazyOptional<IKineticEnergyHandler>> devices = new HashMap<>();
    private double angle;
    private final UUID id;
    private final World level;

    private static long lastNanos = 0;

    public static void onRenderTick(TickEvent.RenderTickEvent event) {
        if(event.phase == TickEvent.Phase.START) {
            long currentNanos = System.nanoTime();
            double deltaSeconds = (currentNanos - lastNanos)/1000000000d;
            lastNanos = currentNanos;
            if(Minecraft.getInstance().isPaused()) return;
            networks.forEach((id, network) -> {
                if(network.speed < 0.0001f) return;
                network.angle += (network.speed * 360f) * deltaSeconds;
                network.angle = network.angle % 360;
            });
        }
    }

    public ClientRotationalNetwork(UUID id, World level) {
        networks.put(id, this);
        this.id = id;
        this.level = level;
    }

    public static void deleteNetwork(UUID id) {
        ClientRotationalNetwork network = networks.remove(id);
        network.devices.forEach((pos, lazy) -> lazy.ifPresent(handler -> handler.setNetwork(null)));
        network.devices.clear();
    }

    public static void deleteAll() {
        Map<UUID, ClientRotationalNetwork> networksCopy = new HashMap<>(networks);
        networksCopy.forEach((id, net) -> deleteNetwork(id));
        networks.clear();
    }

    public static ClientRotationalNetwork getNetwork(UUID id) {
        return networks.get(id);
    }

    public void addTile(BlockPos pos) {
        TileEntity tile = level.getBlockEntity(pos);
        if(tile == null) throw new IllegalStateException("Tile entity from server world does not exist on client world!");
        LazyOptional<IKineticEnergyHandler> lazy = tile.getCapability(ModdedCapabilities.ROTATION);
        lazy.ifPresent(handler -> handler.setNetwork(this));
        devices.put(pos, lazy);
    }

    public void removeTile(BlockPos pos) {
        devices.remove(pos);
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public double getAngle() {
        return angle;
    }

    @Override
    public float getCurrentSpeed() {
        return speed;
    }
}
