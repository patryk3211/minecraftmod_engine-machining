package com.enginemachining.blocks;

import com.enginemachining.EngineMachiningMod;
import com.enginemachining.items.ModdedItems;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.placement.ConfiguredPlacement;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class OreGeneration {
    public static final int maxCopperGenSize = 8;
    public static final int minCopperHeight = 7;
    public static final int maxCopperHeight = 64;

    public static final int maxAluminiumGenSize = 4;
    public static final int minAluminiumHeight = 7;
    public static final int maxAluminiumHeight = 30;

    public static final int maxLeadGenSize = 4;
    public static final int minLeadHeight = 4;
    public static final int maxLeadHeight = 20;

    public static final int maxNickelGenSize = 6;
    public static final int minNickelHeight = 11;
    public static final int maxNickelHeight = 35;

    public static final int maxTinGenSize = 8;
    public static final int minTinHeight = 10;
    public static final int maxTinHeight = 64;

    public static final int maxSilverGenSize = 5;
    public static final int minSilverHeight = 4;
    public static final int maxSilverHeight = 20;

    public static void setupFeatures() {
        for (Ore ore : Ore.values()) {

        }
    }

    public static void onBiomeLoad(BiomeLoadingEvent event) {
        if(event.getCategory() != Biome.Category.NETHER && event.getCategory() != Biome.Category.THEEND) {
            for (Ore ore : Ore.values()) {
                generateOre(event.getGeneration(), OreFeatureConfig.FillerBlockType.NATURAL_STONE, ore);
            }
        }
    }

    private static void generateOre(BiomeGenerationSettingsBuilder generation, RuleTest toReplace, Ore ore) {
        generation.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
                Feature.ORE.configured(new OreFeatureConfig(toReplace, ore.getBlock().defaultBlockState(), ore.getMaxVeinSize()))
                .decorated(Placement.RANGE.configured(new TopSolidRangeConfig(ore.getBottomOffset(), ore.getTopOffset(), ore.getMaxHeight())))
                        .squared().count(ore.getPerChunk()));
    }
}
