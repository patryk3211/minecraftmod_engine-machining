package com.enginemachining.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public abstract class BreakerSwitch extends Block {
    public BreakerSwitch() {
        super(Properties.of(Material.METAL));
    }
}
