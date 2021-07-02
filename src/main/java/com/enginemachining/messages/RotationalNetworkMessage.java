package com.enginemachining.messages;

import com.enginemachining.api.rotation.ClientRotationalNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

public class RotationalNetworkMessage {
    public enum MessageType {
        CREATE_NETWORK((byte) 1),
        ADD_TILES((byte) 2),
        ADD_TILE((byte) 3),
        REMOVE_TILE((byte) 4),
        UPDATE_VELOCITY((byte) 5),
        DELETE_NETWORK((byte) 6),
        DELETE_ALL((byte) 7);

        private final byte id;
        MessageType(byte id) {
            this.id = id;
        }

        public byte getId() {
            return id;
        }

        public static MessageType fromId(byte id) {
            for(MessageType type : MessageType.values()) if(type.getId() == id) return type;
            throw new IllegalArgumentException("Unknown message type id!");
        }
    }

    private final MessageType type;
    private final UUID network_uuid;
    private BlockPos tile;
    private Set<BlockPos> tiles;
    private float speed;

    public RotationalNetworkMessage(MessageType type, UUID network_uuid) {
        this.type = type;
        this.network_uuid = network_uuid;
    }

    public RotationalNetworkMessage setTilePos(BlockPos tilePos) {
        this.tile = tilePos;
        return this;
    }

    public RotationalNetworkMessage setTiles(Set<BlockPos> tiles) {
        this.tiles = tiles;
        return this;
    }

    public RotationalNetworkMessage setSpeed(float speed) {
        this.speed = speed;
        return this;
    }

    public static void encode(RotationalNetworkMessage msg, PacketBuffer buffer) {
        buffer.writeByte(msg.type.getId());
        buffer.writeUUID(msg.network_uuid);
        switch (msg.type) {
            // Message type and network UUID is enough for network delete and network create operations.
            case CREATE_NETWORK:
            case DELETE_NETWORK:
            case DELETE_ALL: break;
            case ADD_TILE:
            case REMOVE_TILE:
                buffer.writeBlockPos(msg.tile);
                break;
            case ADD_TILES:
                int[] data = new int[msg.tiles.size()*3];
                int index = 0;
                for (BlockPos tile : msg.tiles) {
                    data[index] = tile.getX();
                    data[index+1] = tile.getY();
                    data[index+2] = tile.getZ();
                    index+=3;
                }
                buffer.writeVarIntArray(data);
                break;
            case UPDATE_VELOCITY:
                buffer.writeFloat(msg.speed);
                break;
            default: throw new IllegalStateException("Unknown message type detected!");
        }
    }

    public static RotationalNetworkMessage decode(PacketBuffer buffer) {
        MessageType type = MessageType.fromId(buffer.readByte());
        UUID network_uuid = buffer.readUUID();
        RotationalNetworkMessage msg = new RotationalNetworkMessage(type, network_uuid);
        switch (type) {
            // Message type and network UUID is enough for network delete and network create operations.
            case CREATE_NETWORK:
            case DELETE_NETWORK:
            case DELETE_ALL: break;
            case ADD_TILE:
            case REMOVE_TILE:
                msg.setTilePos(buffer.readBlockPos());
                break;
            case ADD_TILES:
                int[] data = buffer.readVarIntArray();
                Set<BlockPos> tiles = new HashSet<>();
                for(int i = 0; i < data.length; i += 3) tiles.add(new BlockPos(data[i], data[i+1], data[i+2]));
                msg.setTiles(tiles);
                break;
            case UPDATE_VELOCITY:
                msg.speed = buffer.readFloat();
                break;
            default: throw new IllegalStateException("Unknown message type detected!");
        }
        return msg;
    }

    public static void handle(RotationalNetworkMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handleClient(msg)));
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleClient(RotationalNetworkMessage msg) {
        switch (msg.type) {
            case CREATE_NETWORK:
                new ClientRotationalNetwork(msg.network_uuid, Minecraft.getInstance().level);
                break;
            case DELETE_NETWORK:
                ClientRotationalNetwork.deleteNetwork(msg.network_uuid);
                break;
            case DELETE_ALL:
                ClientRotationalNetwork.deleteAll();
                break;
            case ADD_TILES: {
                ClientRotationalNetwork network = ClientRotationalNetwork.getNetwork(msg.network_uuid);
                for (BlockPos tile : msg.tiles) {
                    network.addTile(tile);
                }
                break;
            } case ADD_TILE:
                ClientRotationalNetwork.getNetwork(msg.network_uuid).addTile(msg.tile);
                break;
            case REMOVE_TILE:
                ClientRotationalNetwork.getNetwork(msg.network_uuid).removeTile(msg.tile);
                break;
            case UPDATE_VELOCITY:
                ClientRotationalNetwork.getNetwork(msg.network_uuid).setSpeed(msg.speed);
                break;
            default: throw new IllegalStateException("Unknown message type detected!");
        }
    }
}
