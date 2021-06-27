package com.enginemachining.utils;

import com.enginemachining.EngineMachiningMod;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

import java.util.HashMap;
import java.util.Map;

public class WorldDataSaver extends WorldSavedData {
    private final Map<String, INBT> data = new HashMap<>();

    public WorldDataSaver() {
        super(EngineMachiningMod.MOD_ID);
    }

    @Override
    public void load(CompoundNBT nbt) {
        data.clear();
        for (String key : nbt.getAllKeys()) {
            data.put(key, nbt.get(key));
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        data.forEach(nbt::put);
        return nbt;
    }

    public static WorldDataSaver forWorld(ServerWorld world) {
        DimensionSavedDataManager mgr = world.getDataStorage();
        WorldDataSaver saver = mgr.get(WorldDataSaver::new, EngineMachiningMod.MOD_ID);
        if(saver == null) {
            saver = new WorldDataSaver();
            mgr.set(saver);
        }
        return saver;
    }

    public INBT getTag(String name) {
        return data.get(name);
    }

    public void addTag(String name, INBT tag) {
        data.remove(name);
        data.put(name, tag);
    }
}
