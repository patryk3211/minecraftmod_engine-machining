package com.enginemachining.messages;

import com.enginemachining.tileentities.CrusherTile;
import com.enginemachining.tileentities.PowerLimiterTile;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PowerLimiterMessage {
    private BlockPos pos;
    private boolean enabled;

    public PowerLimiterMessage(BlockPos pos, boolean enabled) {
        this.pos = pos;
        this.enabled = enabled;
    }

    public static void encode(PowerLimiterMessage msg, PacketBuffer buffer) {
        buffer.writeBlockPos(msg.pos);
        buffer.writeBoolean(msg.enabled);
    }

    public static PowerLimiterMessage decode(PacketBuffer buffer) {
        return new PowerLimiterMessage(buffer.readBlockPos(), buffer.readBoolean());
    }

    public static void handle(PowerLimiterMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if(ctx.get().getSender().level.isAreaLoaded(msg.pos, 1)) {
                TileEntity te = ctx.get().getSender().level.getBlockEntity(msg.pos);
                if(te instanceof PowerLimiterTile) {
                    PowerLimiterTile plt = (PowerLimiterTile)te;
                    plt.data.set(4, msg.enabled ? 1 : 0);
                    plt.getLevel().sendBlockUpdated(plt.getBlockPos(), plt.getBlockState(), plt.getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
                    plt.setChanged();
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
