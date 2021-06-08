package com.enginemachining.utils;

import com.enginemachining.handlers.IEnergyReceiver;
import com.enginemachining.handlers.IEnergySender;
import com.enginemachining.tileentities.EnergyWireTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class PipeTracer {
    public static Collection<IEnergyReceiver> TraceForReceivers(IEnergySender sender) {
        List<BlockPos> traced = new ArrayList<>();

        Collection<IEnergyReceiver> receivers = TraceForReceivers(sender.getTileEntity().getLevel(), sender.getTileEntity().getBlockPos(), traced);

        for(IEnergyReceiver rec : receivers) {
            System.out.println(((TileEntity)rec).getBlockPos());
        }

        return receivers;
    }

    private static Collection<IEnergyReceiver> TraceForReceivers(World world, BlockPos currentPos, List<BlockPos> traced) {
        List<IEnergyReceiver> receivers = new ArrayList<>();

        traced.add(currentPos);

        for(Direction dir : Direction.values()) {
            BlockPos neighborPos = currentPos.offset(dir.getNormal());
            TileEntity te = world.getBlockEntity(currentPos);
            if(te instanceof EnergyWireTile) {
                if(!((EnergyWireTile) te).isSideConnectable(dir)) continue;
            }
            TileEntity nte = world.getBlockEntity(neighborPos);
            if(nte instanceof EnergyWireTile) {
                if(!((EnergyWireTile) nte).isSideConnectable(dir.getOpposite())) continue;
            }

            if(nte instanceof EnergyWireTile && !traced.contains(neighborPos)) receivers.addAll(TraceForReceivers(world, neighborPos, traced));

            if(nte instanceof IEnergyReceiver) {
                if(nte.getCapability(CapabilityEnergy.ENERGY, dir).isPresent()) {
                    receivers.add((IEnergyReceiver)nte);
                }
            }
        }

        return receivers;
    }
}
