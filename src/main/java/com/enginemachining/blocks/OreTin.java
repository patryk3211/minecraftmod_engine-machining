package com.enginemachining.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class OreTin extends Block {
    public OreTin() {
        super(Properties.create(Material.ROCK)
                .hardnessAndResistance(2.9f, 2.9f));
        setRegistryName("enginemachining:ore_tin");
    }
}
