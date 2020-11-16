package com.enginemachining.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class OreTin extends Block {
    public OreTin() {
        super(Properties.create(Material.IRON)
                .hardnessAndResistance(2.9f, 2.9f).harvestLevel(1).harvestTool(ToolType.PICKAXE));
        setRegistryName("enginemachining:ore_tin");
    }
}
