package com.enginemachining.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class OreLead extends Block {
    public OreLead() {
        super(Properties.of(Material.STONE)
                .strength(3.0f, 3.0f)
                .harvestLevel(2)
                .harvestTool(ToolType.PICKAXE));
    }
}
