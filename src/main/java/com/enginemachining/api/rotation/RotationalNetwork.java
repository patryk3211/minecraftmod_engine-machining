package com.enginemachining.api.rotation;

import com.enginemachining.EngineMachiningPacketHandler;
import com.enginemachining.capabilities.ModdedCapabilities;
import com.enginemachining.messages.RotationalNetworkMessage;
import com.enginemachining.utils.WorldDataSaver;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class RotationalNetwork implements IRotationalNetwork {
    private static final Map<RegistryKey<World>, Set<RotationalNetwork>> networks = new HashMap<>();
    // Map of players in dimensions with a map of rotational networks tracked by players
    private static final Map<RegistryKey<World>, Map<ServerPlayerEntity, Set<RotationalNetwork>>> players = new HashMap<>();
    private static final List<Runnable> playersToTick = new ArrayList<>();

    public Set<ServerPlayerEntity> trackers = new HashSet<>();

    private double speed;
    private final Map<BlockPos, LazyOptional<IKineticEnergyHandler>> devices = new HashMap<>();
    private final World level;
    private float inertia;
    private float friction;
    private final UUID id;

    public static void registerEvents() {
        MinecraftForge.EVENT_BUS.addListener(RotationalNetwork::onWorldLoad);
        MinecraftForge.EVENT_BUS.addListener(RotationalNetwork::onWorldSave);
        MinecraftForge.EVENT_BUS.addListener(RotationalNetwork::onWorldUnload);
        MinecraftForge.EVENT_BUS.addListener(RotationalNetwork::onPlayerJoin);
        MinecraftForge.EVENT_BUS.addListener(RotationalNetwork::onPlayerLeave);
        MinecraftForge.EVENT_BUS.addListener(RotationalNetwork::onPlayerChangeDimension);
        MinecraftForge.EVENT_BUS.addListener(RotationalNetwork::onTick);
    }

    private static void onTick(TickEvent.WorldTickEvent event) {
        if(event.side == LogicalSide.CLIENT) return;
        if(event.phase == TickEvent.Phase.START) {
            if(!networks.containsKey(event.world.dimension())) return;
            if(!players.containsKey(event.world.dimension())) return;
            for (RotationalNetwork network : networks.get(event.world.dimension())) {
                if(network.isNetworkLoaded()) {
                    for (BlockPos position : network.devices.keySet()) {
                        if(network.devices.get(position) != null) continue;
                        TileEntity te = network.level.getBlockEntity(position);
                        // TODO: [02.07.2021] Maybe don't crash the entire game when tile entity was not found, try to recover.
                        if(te == null) throw new IllegalStateException("Tile entity does not exist!");
                        LazyOptional<IKineticEnergyHandler> cap = te.getCapability(ModdedCapabilities.ROTATION);
                        cap.ifPresent(handler -> handler.setNetwork(network));
                        network.devices.replace(position, cap);
                    }
                    network.recalculateNetwork(false);
                    players.get(event.world.dimension()).forEach((player, trackedNetworks) -> {
                        if(!trackedNetworks.contains(network)) {
                            sendNetwork(PacketDistributor.PLAYER.with(() -> player), network);
                            trackedNetworks.add(network);
                            network.trackers.add(player);
                        }
                    });
                } else {
                    for (BlockPos position : network.devices.keySet()) {
                        if(network.devices.get(position) != null) continue;
                        network.devices.replace(position, null);
                    }
                    players.get(event.world.dimension()).forEach((player, trackedNetworks) -> {
                        if(trackedNetworks.contains(network)) {
                            EngineMachiningPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new RotationalNetworkMessage(RotationalNetworkMessage.MessageType.DELETE_NETWORK, network.getId()));
                            trackedNetworks.remove(network);
                            network.trackers.remove(player);
                        }
                    });
                }
            }
        }
        if(event.phase == TickEvent.Phase.END) {
            List<Runnable> copy = new ArrayList<>(playersToTick);
            copy.forEach(Runnable::run);
            playersToTick.removeAll(copy);
            if(networks.containsKey(event.world.dimension())) {
                Set<RotationalNetwork> nets = networks.get(event.world.dimension());
                for (RotationalNetwork network : nets) {
                    if(!network.isNetworkLoaded()) continue;
                    int sign = 0;
                    if(network.speed > 0) sign = 1;
                    else if(network.speed < 0) sign = -1;
                    network.applyTickForce(network.friction * -sign);
                }
            }
        }
    }

    private static void onWorldLoad(WorldEvent.Load event) {
        if(!event.getWorld().isClientSide() && event.getWorld() instanceof ServerWorld) {
            ServerWorld world = (ServerWorld) event.getWorld();
            WorldDataSaver saver = WorldDataSaver.forWorld(world);
            INBT list = saver.getTag("rotational_networks");
            if(!(list instanceof ListNBT)) return;
            for (INBT entry : ((ListNBT) list)) {
                if(!(entry instanceof CompoundNBT)) continue;
                RotationalNetwork network = new RotationalNetwork(world);
                CompoundNBT netTag = (CompoundNBT) entry;
                network.speed = netTag.getFloat("speed");
                INBT blocksINBT = netTag.get("blocks");
                if(!(blocksINBT instanceof ListNBT)) continue;
                ListNBT blocks = (ListNBT) blocksINBT;
                for (INBT blockEntry : blocks) {
                    if(!(blockEntry instanceof CompoundNBT)) continue;
                    CompoundNBT block = (CompoundNBT) blockEntry;
                    BlockPos position = new BlockPos(block.getInt("x"), block.getInt("y"), block.getInt("z"));
                    network.devices.put(position, null);
                }
                if(networks.containsKey(world.dimension())) networks.get(world.dimension()).add(network);
                else {
                    Set<RotationalNetwork> nets = new HashSet<>();
                    nets.add(network);
                    networks.put(world.dimension(), nets);
                }
            }
        }
    }

    private static void onWorldSave(WorldEvent.Save event) {
        if(!event.getWorld().isClientSide() && event.getWorld() instanceof ServerWorld) {
            ServerWorld world = (ServerWorld) event.getWorld();
            WorldDataSaver saver = WorldDataSaver.forWorld(world);
            ListNBT list = new ListNBT();
            networks.forEach((dim, nets) -> {
                if(!world.dimension().equals(dim)) return;
                for (RotationalNetwork network : nets) {
                    if(network.isEmpty()) continue;
                    CompoundNBT netTag = new CompoundNBT();
                    netTag.putFloat("speed", network.getCurrentSpeed());
                    ListNBT blocks = new ListNBT();
                    for (BlockPos position : network.devices.keySet()) {
                        CompoundNBT block = new CompoundNBT();
                        block.putInt("x", position.getX());
                        block.putInt("y", position.getY());
                        block.putInt("z", position.getZ());
                        blocks.add(block);
                    }
                    netTag.put("blocks", blocks);
                    list.add(netTag);
                }
            });
            saver.addTag("rotational_networks", list);
            saver.setDirty();
        }
    }

    private static void sendNetwork(PacketDistributor.PacketTarget target, RotationalNetwork network) {
        EngineMachiningPacketHandler.INSTANCE.send(target, new RotationalNetworkMessage(RotationalNetworkMessage.MessageType.CREATE_NETWORK, network.getId()));
        EngineMachiningPacketHandler.INSTANCE.send(target, new RotationalNetworkMessage(RotationalNetworkMessage.MessageType.ADD_TILES, network.getId()).setTiles(network.devices.keySet()));
    }

    private static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if(event.getPlayer() instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
            if(players.containsKey(event.getFrom())) players.get(event.getFrom()).remove(player);
            if(players.containsKey(event.getTo())) players.get(event.getTo()).put(player, new HashSet<>());
            else {
                Map<ServerPlayerEntity, Set<RotationalNetwork>> list = new HashMap<>();
                list.put(player, new HashSet<>());
                players.put(event.getTo(), list);
            }
            PacketDistributor.PacketTarget target = PacketDistributor.PLAYER.with(() -> player);
            EngineMachiningPacketHandler.INSTANCE.send(target, new RotationalNetworkMessage(RotationalNetworkMessage.MessageType.DELETE_ALL, new UUID(0, 0)));
            if(networks.containsKey(event.getTo())) networks.get(event.getTo()).forEach(net -> sendNetwork(target, net));
            // TODO: [27.06.2021] Synchronize network's velocity on dimension change.
        }
    }

    private static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if(event.getPlayer() instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
            RegistryKey<World> dim = player.getLevel().dimension();
            if(players.containsKey(dim)) {
                players.get(dim).put(player, new HashSet<>());
            } else {
                Map<ServerPlayerEntity, Set<RotationalNetwork>> list = new HashMap<>();
                list.put(player, new HashSet<>());
                players.put(dim, list);
            }
            playersToTick.add(() -> {
                PacketDistributor.PacketTarget target = PacketDistributor.PLAYER.with(() -> player);
                EngineMachiningPacketHandler.INSTANCE.send(target, new RotationalNetworkMessage(RotationalNetworkMessage.MessageType.DELETE_ALL, new UUID(0, 0)));
                if(networks.containsKey(dim)) networks.get(dim).forEach(net -> sendNetwork(target, net));
            });
            // TODO: [27.06.2021] Synchronize network's velocity on join.
        }
    }

    private static void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        if(event.getPlayer() instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
            RegistryKey<World> dim = player.getLevel().dimension();
            if(players.containsKey(dim)) players.get(dim).remove(player);
        }
    }

    private static void onWorldUnload(WorldEvent.Unload event) {
        networks.clear();
        players.clear();
        playersToTick.clear();
    }

    private RotationalNetwork(World level) {
        if(networks.containsKey(level.dimension())) {
            networks.get(level.dimension()).add(this);
        } else {
            Set<RotationalNetwork> nets = new HashSet<>();
            nets.add(this);
            networks.put(level.dimension(), nets);
        }
        id = UUID.randomUUID();
        this.level = level;
        if(players.containsKey(level.dimension())) players.get(level.dimension()).forEach((player, nets) -> sendNetwork(PacketDistributor.PLAYER.with(() -> player), this));
    }

    public UUID getId() {
        return id;
    }

    private static Map<Direction, TileEntity> getNetworksAround(BlockPos pos, World level) {
        TileEntity device = level.getBlockEntity(pos);
        if(device == null) return null;
        Map<Direction, TileEntity> ret = new HashMap<>();
        for(Direction dir : Direction.values()) {
            LazyOptional<IKineticEnergyHandler> cap = device.getCapability(ModdedCapabilities.ROTATION, dir);
            if(!cap.isPresent()) continue;
            TileEntity neighbour = level.getBlockEntity(pos.offset(dir.getNormal()));
            if(neighbour == null) continue;
            LazyOptional<IKineticEnergyHandler> neiCap = neighbour.getCapability(ModdedCapabilities.ROTATION, dir.getOpposite());
            if(!neiCap.isPresent()) continue;
            ret.put(dir, neighbour);
        }
        return ret;
    }

    /**
     * This method will add a kinetic device to a network around it, if none were found it will create one.
     * If more than one network was found, they will be merged.
     * @param position Position of the device.
     * @param level Level of the device.
     */
    public static void addToNetwork(BlockPos position, World level) {
        TileEntity device = level.getBlockEntity(position);
        if(device == null) return;
        Map<Direction, TileEntity> devices = getNetworksAround(position, level);
        if(devices == null) return;
        Set<RotationalNetwork> nets = new HashSet<>();
        devices.forEach((dir, dev) -> {
            LazyOptional<IKineticEnergyHandler> cap = dev.getCapability(ModdedCapabilities.ROTATION, dir.getOpposite());
            cap.ifPresent(handler -> { if(handler.getNetwork() != null) nets.add((RotationalNetwork) handler.getNetwork()); });
        });

        LazyOptional<IKineticEnergyHandler> cap = device.getCapability(ModdedCapabilities.ROTATION);
        AtomicBoolean hasNetwork = new AtomicBoolean(false);
        cap.ifPresent(handler -> hasNetwork.set(handler.getNetwork() != null));
        if(hasNetwork.get()) return;

        if(networks.containsKey(level.dimension())) networks.get(level.dimension()).forEach(network -> {
            if(network.devices.containsKey(position)) {
                hasNetwork.set(true);
                cap.ifPresent(handler -> handler.setNetwork(network));
                network.devices.replace(position, cap);
            }
        });
        if(hasNetwork.get()) return;

        if(nets.size() == 0) {
            // No networks found around.
            RotationalNetwork net = new RotationalNetwork(level);
            cap.ifPresent(handler -> handler.setNetwork(net));
            net.devices.put(position, cap);
            if(players.containsKey(level.dimension())) players.get(level.dimension()).forEach((player, list) -> EngineMachiningPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new RotationalNetworkMessage(RotationalNetworkMessage.MessageType.ADD_TILE, net.id).setTilePos(position)));
            net.recalculateNetwork(true);
        } else if(nets.size() == 1) {
            // Only one network found, add this handler to it.
            for (RotationalNetwork net : nets) {
                net.devices.put(position, cap);
                cap.ifPresent(handler -> {
                    handler.setNetwork(net);
                    net.recalculateNetwork(true);
                });
                if(players.containsKey(level.dimension())) players.get(level.dimension()).forEach((player, list) -> EngineMachiningPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new RotationalNetworkMessage(RotationalNetworkMessage.MessageType.ADD_TILE, net.id).setTilePos(position)));
            }
        } else {
            // More than one network found, merge them all together.
            RotationalNetwork main = null;
            Set<BlockPos> newTiles = new HashSet<>();
            newTiles.add(position);
            AtomicReference<Float> inertiaCombined = new AtomicReference<>((float) 0);
            AtomicReference<Float> frictionCombined = new AtomicReference<>((float) 0);
            float speedCombined = 0;
            for(RotationalNetwork net : nets) {
                speedCombined += net.getCurrentSpeed();
                frictionCombined.updateAndGet(v -> (v + net.friction));
                inertiaCombined.updateAndGet(v -> (v + net.inertia));

                if(main == null) {
                    main = net;
                    continue;
                }
                RotationalNetwork finalMain = main;
                net.devices.forEach((pos, lazy) -> {
                    finalMain.devices.put(pos, lazy);
                    newTiles.add(pos);
                    lazy.ifPresent(handler -> handler.setNetwork(finalMain));
                });
                net.trackers.forEach(player -> {
                    EngineMachiningPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new RotationalNetworkMessage(RotationalNetworkMessage.MessageType.DELETE_NETWORK, net.getId()));
                    if(!finalMain.trackers.contains(player)) {
                        finalMain.trackers.add(player);
                        sendNetwork(PacketDistributor.PLAYER.with(() -> player), finalMain);
                    }
                });

                networks.get(net.level.dimension()).remove(net);
            }
            RotationalNetwork finalMain = main;
            cap.ifPresent(handler -> {
                handler.setNetwork(finalMain);
                inertiaCombined.updateAndGet(v -> (v + handler.getInertiaMass()));
                frictionCombined.updateAndGet(v -> (v + handler.getFriction()));
            });
            main.trackers.forEach(player -> EngineMachiningPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new RotationalNetworkMessage(RotationalNetworkMessage.MessageType.ADD_TILES, finalMain.getId()).setTiles(newTiles)));
            main.devices.put(position, cap);

            // TODO: [02.07.2021] Check if this makes any sense.
            main.speed = speedCombined/inertiaCombined.get();
            main.inertia = inertiaCombined.get();
            main.friction = frictionCombined.get();
        }
    }

    private RotationalNetwork traceNewNetwork(BlockPos start) {
        RotationalNetwork network = new RotationalNetwork(level);
        traceIntoNetwork(start, network);
        return network;
    }

    private void traceIntoNetwork(BlockPos position, RotationalNetwork network) {
        if(!devices.containsKey(position)) return;
        TileEntity device = level.getBlockEntity(position);
        if(device == null) return;
        LazyOptional<IKineticEnergyHandler> cap = device.getCapability(ModdedCapabilities.ROTATION);
        cap.ifPresent(handler -> {
            RotationalNetwork oldNet = (RotationalNetwork) handler.getNetwork();
            if(oldNet != null) {
                oldNet.devices.remove(position);
                handler.setNetwork(null);
            }
            handler.setNetwork(network);
            network.devices.put(position, cap);
            for(Direction dir : Direction.values()) {
                if(!device.getCapability(ModdedCapabilities.ROTATION, dir).isPresent()) continue;
                BlockPos neighbourPos = position.offset(dir.getNormal());
                if(!devices.containsKey(neighbourPos)) continue;
                TileEntity neighbour = level.getBlockEntity(neighbourPos);
                if(neighbour == null) continue;
                if(!neighbour.getCapability(ModdedCapabilities.ROTATION, dir.getOpposite()).isPresent()) continue;
                traceIntoNetwork(neighbourPos, network);
            }
        });
    }

    /**
     * This network will remove a kinetic energy device from it's rotational network.
     * You have to call this method BEFORE you remove the tile entity
     * @param pos Position of the device
     * @param level Level of the device
     */
    public static void removeFromNetwork(BlockPos pos, World level) {
        if(level.isClientSide) throw new IllegalStateException("This method should not be run on client dist!");
        TileEntity device = level.getBlockEntity(pos);
        if(device == null) return;
        device.getCapability(ModdedCapabilities.ROTATION).ifPresent(handler -> {
            RotationalNetwork network = (RotationalNetwork) handler.getNetwork();
            if(network == null) return;
            network.devices.remove(pos);
            handler.setNetwork(null);
            if(network.isEmpty()) {
                networks.get(network.level.dimension()).remove(network);
                return;
            }
            int devCountAround = 0;
            for(Direction dir : Direction.values()) {
                if(network.devices.containsKey(pos.offset(dir.getNormal()))) devCountAround++;
            }
            if(devCountAround == 1) network.recalculateNetwork(true);
            if(devCountAround > 1) {
                network.trackers.forEach(player -> EngineMachiningPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new RotationalNetworkMessage(RotationalNetworkMessage.MessageType.DELETE_NETWORK, network.id)));
                for(Direction dir : Direction.values()) {
                    RotationalNetwork newNet = network.traceNewNetwork(pos.offset(dir.getNormal()));
                    if(newNet.isEmpty()) networks.get(newNet.level.dimension()).remove(newNet);
                    newNet.trackers.addAll(network.trackers);
                    newNet.trackers.forEach(player -> sendNetwork(PacketDistributor.PLAYER.with(() -> player), newNet));
                    newNet.recalculateNetwork(true);
                    newNet.speed = network.getCurrentSpeed();
                    newNet.speedChanged();
                }
                networks.get(network.level.dimension()).remove(network);
            }
        });
    }

    private void speedChanged() {
        // TODO: [27.06.2021] Make it so that only players within a certain distance from the network receive speed updates.
        trackers.forEach(player -> EngineMachiningPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new RotationalNetworkMessage(RotationalNetworkMessage.MessageType.UPDATE_VELOCITY, id).setSpeed(getCurrentSpeed())));
    }

    private void recalculateNetwork(boolean affectVelocity) {
        float startInertia = inertia;
        inertia = 0;
        friction = 0;
        devices.forEach((pos, lazy) -> lazy.ifPresent(handler -> {
            inertia += handler.getInertiaMass();
            friction += handler.getFriction();
        }));
        float inertiaChange = startInertia/inertia;
        if(affectVelocity && inertiaChange < 1) speed *= inertiaChange;
    }

    /**
     * This method will apply a force to this network for the time of 1 tick (1/20th of a second) to change it's speed.
     * @param force The force to be applied.
     */
    public void applyTickForce(float force) {
        if(inertia == 0) return;
        double acceleration = force/inertia;
        double deltaSpeed = acceleration*(1/20f);
        speed += deltaSpeed;
        if(Math.abs(speed) < 0.00001) speed = 0;
        if(Math.round(deltaSpeed*1000) != 0) speedChanged();
    }

    /**
     * This method will apply a force in the opposite direction of the current rotation direction to this network for the time of 1 tick to change it's speed.
     * @param force The force to be applied.
     */
    public void applyCounterTickForce(float force) {
        if(inertia == 0) return;
        double acceleration = force/inertia;
        double deltaSpeed = acceleration*(1/20f);
        if(speed > 0) speed -= deltaSpeed;
        else if(speed < 0) speed += deltaSpeed;
        if(Math.abs(speed) < 0.00001) speed = 0;
        if(Math.round(deltaSpeed*1000) != 0) speedChanged();
    }

    /**
     * This method calculates the required force to be applied over the time of 1 tick, to change the speed of this network to the target speed.
     * @param targetSpeed Speed to be reached.
     * @return Force that needs to be applied.
     */
    public float calculateForceForSpeed(float targetSpeed) {
        double deltaSpeed = targetSpeed - speed;
        double acceleration = deltaSpeed/(1/20f);
        return (float)(acceleration*inertia);
    }

    /**
     * This method is used to get the current rotational speed of this network.
     * @return Rotational speed
     */
    public float getCurrentSpeed() {
        return (float)speed;
    }

    /**
     * This method checks if a network is loaded by any player.
     * @return Returns true if any block of this network is loaded.
     */
    public boolean isNetworkLoaded() {
        for (BlockPos pos : devices.keySet()) {
            if(level.isLoaded(pos)) return true;
        }
        return false;
    }

    /**
     * Checks if this network is empty.
     * @return Returns true if this network does not have any devices.
     */
    public boolean isEmpty() {
        return devices.isEmpty();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof RotationalNetwork) {
            return devices.values().equals(((RotationalNetwork) obj).devices.values());
        }
        return false;
    }
}
