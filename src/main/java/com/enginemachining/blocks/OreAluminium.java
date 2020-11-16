package com.enginemachining.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class OreAluminium extends Block {
    public OreAluminium() {
        super(Properties.create(Material.IRON)
                .hardnessAndResistance( 3.5f, 3.5f)
                .harvestLevel(1)
                .harvestTool(ToolType.PICKAXE)
                .setRequiresTool());
        setRegistryName("enginemachining:ore_aluminium");
    }
}
