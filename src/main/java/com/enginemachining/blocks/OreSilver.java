package com.enginemachining.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class OreSilver extends Block {
    public OreSilver() {
        super(Properties.create(Material.ROCK)
                .hardnessAndResistance(2.8f, 2.8f).harvestLevel(2));
        setRegistryName("enginemachining:ore_silver");
    }
}
