package com.enginemachining.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class OreCopper extends Block {
    public OreCopper() {
        super(Properties.create(Material.ROCK)
                .hardnessAndResistance( 3.0f, 3.0f)
                .harvestLevel(1)
                .harvestTool(ToolType.PICKAXE)
                .setRequiresTool());
    }
}
