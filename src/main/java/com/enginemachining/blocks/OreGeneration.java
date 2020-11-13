package com.enginemachining.blocks;

import com.enginemachining.items.ModdedItems;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureSpread;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

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

    public static void SetupFeatureGeneration() {
        RegisterOreGenFeature(ModdedBlocks.ore_copper, maxCopperGenSize, minCopperHeight, maxCopperHeight);
        RegisterOreGenFeature(ModdedBlocks.ore_aluminium, maxAluminiumGenSize, minAluminiumHeight, maxAluminiumHeight);
        RegisterOreGenFeature(ModdedBlocks.ore_tin, maxTinGenSize, minTinHeight, maxTinHeight);
        RegisterOreGenFeature(ModdedBlocks.ore_lead, maxLeadGenSize, minLeadHeight, maxLeadHeight);
        RegisterOreGenFeature(ModdedBlocks.ore_nickel, maxNickelGenSize, minNickelHeight, maxNickelHeight);
        RegisterOreGenFeature(ModdedBlocks.ore_silver, maxSilverGenSize, minSilverHeight, maxSilverHeight);

        for (Map.Entry<RegistryKey<Biome>, Biome> biome : WorldGenRegistries.BIOME.getEntries()) {
            if (!biome.getValue().getCategory().equals(Biome.Category.NETHER) && !biome.getValue().getCategory().equals(Biome.Category.THEEND)) {
                AddFeature(biome.getValue(),
                        GenerationStage.Decoration.UNDERGROUND_ORES,
                        WorldGenRegistries.CONFIGURED_FEATURE.getOrDefault(ModdedBlocks.ore_copper.getRegistryName()));
                AddFeature(biome.getValue(),
                        GenerationStage.Decoration.UNDERGROUND_ORES,
                        WorldGenRegistries.CONFIGURED_FEATURE.getOrDefault(ModdedBlocks.ore_aluminium.getRegistryName()));
                AddFeature(biome.getValue(),
                        GenerationStage.Decoration.UNDERGROUND_ORES,
                        WorldGenRegistries.CONFIGURED_FEATURE.getOrDefault(ModdedBlocks.ore_tin.getRegistryName()));
                AddFeature(biome.getValue(),
                        GenerationStage.Decoration.UNDERGROUND_ORES,
                        WorldGenRegistries.CONFIGURED_FEATURE.getOrDefault(ModdedBlocks.ore_lead.getRegistryName()));
                AddFeature(biome.getValue(),
                        GenerationStage.Decoration.UNDERGROUND_ORES,
                        WorldGenRegistries.CONFIGURED_FEATURE.getOrDefault(ModdedBlocks.ore_nickel.getRegistryName()));
                AddFeature(biome.getValue(),
                        GenerationStage.Decoration.UNDERGROUND_ORES,
                        WorldGenRegistries.CONFIGURED_FEATURE.getOrDefault(ModdedBlocks.ore_silver.getRegistryName()));
            }
        }
    }

    public static void AddFeature(Biome biome, GenerationStage.Decoration decoration, ConfiguredFeature<?, ?> feature) {
        List<List<Supplier<ConfiguredFeature<?, ?>>>> biomeFeatures = new ArrayList<>(
                biome.getGenerationSettings().getFeatures()
        );

        while (biomeFeatures.size() <= decoration.ordinal()) {
            biomeFeatures.add(Lists.newArrayList());
        }

        List<Supplier<ConfiguredFeature<?, ?>>> features = new ArrayList<>(biomeFeatures.get(decoration.ordinal()));
        features.add(() -> feature);
        biomeFeatures.set(decoration.ordinal(), features);

        ObfuscationReflectionHelper.setPrivateValue(BiomeGenerationSettings.class, biome.getGenerationSettings(), biomeFeatures, "features");
    }

    public static void RegisterOreGenFeature(Block block, int maxSize, int minHeight, int maxHeight) {
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE,
                block.getRegistryName(),
                Feature.ORE.withConfiguration(
                        new OreFeatureConfig(
                                OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD,
                                block.getDefaultState(),
                                maxSize))
                        .withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(minHeight, 0, maxHeight)))
                        .square()
                        .func_242731_b(maxSize));
    }
}
