package com.enginemachining.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class OreSilver extends Block {
    public OreSilver() {
        super(Properties.create(Material.ROCK)
                .hardnessAndResistance(2.8f, 2.8f)
                .harvestLevel(2)
                .harvestTool(ToolType.PICKAXE)
                .setRequiresTool());
    }
}
