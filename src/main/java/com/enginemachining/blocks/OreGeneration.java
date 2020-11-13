package com.enginemachining.blocks;

import net.minecraftforge.common.BiomeManager;

public class OreGeneration {
    public static void SetupOreGen() {
        BiomeManager.getBiomes(BiomeManager.BiomeType.WARM).forEach((BiomeManager.BiomeEntry biomeEntry) -> {

        });
    }
}
