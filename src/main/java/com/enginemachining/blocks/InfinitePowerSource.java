package com.enginemachining.blocks;

import com.enginemachining.tileentities.InfinitePowerSourceTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class InfinitePowerSource extends Block {
    public InfinitePowerSource() {
        super(Properties.of(Material.METAL).strength(-1f));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new InfinitePowerSourceTile();
    }
}
