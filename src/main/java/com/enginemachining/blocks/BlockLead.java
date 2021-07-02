package com.enginemachining.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class BlockLead extends Block{

    public BlockLead(){
    super(Properties.of(Material.METAL)
                .strength( 4.0f, 5.0f)
                .harvestLevel(1)
                .harvestTool(ToolType.PICKAXE));
}
    }

