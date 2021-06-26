package com.enginemachining.api.rotation;

import com.enginemachining.api.rotation.IKineticEnergyHandler;
import com.enginemachining.capabilities.ModdedCapabilities;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RotationalNetwork {
    private float speed;
    private Map<BlockPos, LazyOptional<IKineticEnergyHandler>> devices;

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

    public static void addToNetwork(BlockPos position, World level) {
        TileEntity device = level.getBlockEntity(position);
        if(device == null) return;
        Map<Direction, TileEntity> devices = getNetworksAround(position, level);
        if(devices == null) return;
        Set<RotationalNetwork> nets = new HashSet<>();
        devices.forEach((dir, dev) -> {
            LazyOptional<IKineticEnergyHandler> cap = dev.getCapability(ModdedCapabilities.ROTATION, dir.getOpposite());
            cap.ifPresent(handler -> nets.add(handler.getNetwork()));
        });
        LazyOptional<IKineticEnergyHandler> cap = device.getCapability(ModdedCapabilities.ROTATION);
        if(nets.size() == 0) {
            // No networks found around.
            cap.ifPresent(handler -> handler.setNetwork(new RotationalNetwork()));
        } else if(nets.size() == 1) {
            // Only one network found, add this handler to it.

        }
    }

}
