package com.enginemachining.blocks;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;

public class OreGeneration {
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
