package com.enginemachining.blocks;

import com.enginemachining.handlers.InfiniteEnergySource;
import com.enginemachining.tileentities.DebugEnergySourceTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public class DebugEnergySource extends Block {
    public DebugEnergySource() {
        super(Properties.of(Material.METAL).strength(-1f));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new DebugEnergySourceTile();
    }
}
