package com.enginemachining.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class OreTin extends Block {
    public OreTin() {
        super(Properties.of(Material.STONE)
                .strength(2.9f, 2.9f)
                .harvestLevel(1)
                .harvestTool(ToolType.PICKAXE));
    }
}
