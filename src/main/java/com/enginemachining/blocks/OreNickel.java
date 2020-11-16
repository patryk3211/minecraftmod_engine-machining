package com.enginemachining.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class OreNickel extends Block {
    public OreNickel() {
        super(Properties.create(Material.ROCK)
                .hardnessAndResistance(3.0f, 3.0f).harvestLevel(2));
        setRegistryName("enginemachining:ore_nickel");
    }
}
