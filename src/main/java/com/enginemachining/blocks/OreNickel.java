package com.enginemachining.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class OreNickel extends Block {
    public OreNickel() {
        super(Properties.of(Material.METAL)
                .strength(3.0f, 3.0f)
                .harvestLevel(2)
                .harvestTool(ToolType.PICKAXE));
    }
}
