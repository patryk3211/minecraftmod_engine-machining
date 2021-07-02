package com.enginemachining.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class BlockCopper extends Block{

    public BlockCopper(){
    super(Properties.of(Material.METAL)
                .strength( 5.0f, 6.0f)
                .harvestLevel(2)
                .harvestTool(ToolType.PICKAXE));
}
    }

