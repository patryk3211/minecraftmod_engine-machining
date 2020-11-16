package com.enginemachining.messages;

import com.enginemachining.tileentities.CrusherTile;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CrusherTileMessage {
    BlockPos pos;
    boolean enabled;

    public CrusherTileMessage(BlockPos pos, boolean enabled) {
        this.pos = pos;
        this.enabled = enabled;
    }

    public static void encode(CrusherTileMessage msg, PacketBuffer buffer) {
        buffer.writeBlockPos(msg.pos);
        buffer.writeBoolean(msg.enabled);
    }

    public static CrusherTileMessage decode(PacketBuffer buffer) {
        CrusherTileMessage msg = new CrusherTileMessage(buffer.readBlockPos(), buffer.readBoolean());
        return msg;
    }

    public static void handle(CrusherTileMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if(ctx.get().getSender().world.isAreaLoaded(msg.pos, 1)) {
                TileEntity te = ctx.get().getSender().world.getTileEntity(msg.pos);
                if(te instanceof CrusherTile) {
                    CrusherTile ct = (CrusherTile)te;
                    ct.trackedData.set(2, msg.enabled ? 1 : 0);
                    ct.getWorld().notifyBlockUpdate(ct.getPos(), ct.getBlockState(), ct.getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
                    ct.markDirty();
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
