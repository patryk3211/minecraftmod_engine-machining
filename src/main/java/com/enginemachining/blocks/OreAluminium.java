package com.enginemachining.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class OreAluminium extends Block {
    public OreAluminium() {
        super(Properties.create(Material.ROCK)
                .hardnessAndResistance( 3.5f, 3.5f).harvestLevel(1));
        setRegistryName("enginemachining:ore_aluminium");
    }
}
