package com.enginemachining.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class BlockAluminium extends Block{

    public BlockAluminium(){
    super(Properties.of(Material.METAL)
                .strength( 4.0f, 5.0f)
                .harvestLevel(1)
                .harvestTool(ToolType.PICKAXE));
}
    }

