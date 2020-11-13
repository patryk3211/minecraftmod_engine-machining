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
    public static final int maxAluminiumHeight = 25;

    public static void SetupFeatureGeneration() {
        RegisterOreGenFeature(ModdedBlocks.ore_copper, maxCopperGenSize, minCopperHeight, maxCopperHeight);
        RegisterOreGenFeature(ModdedBlocks.ore_aluminium, maxAluminiumGenSize, minAluminiumHeight, maxAluminiumHeight);


        for (Map.Entry<RegistryKey<Biome>, Biome> biome : WorldGenRegistries.BIOME.getEntries()) {
            if (!biome.getValue().getCategory().equals(Biome.Category.NETHER) && !biome.getValue().getCategory().equals(Biome.Category.THEEND)) {
                AddFeature(biome.getValue(),
                        GenerationStage.Decoration.UNDERGROUND_ORES,
                        WorldGenRegistries.CONFIGURED_FEATURE.getOrDefault(ModdedBlocks.ore_copper.getRegistryName()));
                AddFeature(biome.getValue(),
                        GenerationStage.Decoration.UNDERGROUND_ORES,
                        WorldGenRegistries.CONFIGURED_FEATURE.getOrDefault(ModdedBlocks.ore_aluminium.getRegistryName()));
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
                        .func_242731_b(maxSize));
    }
}
