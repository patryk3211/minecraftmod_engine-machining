package com.enginemachining.blocks;

import com.enginemachining.tileentities.HandCrankTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class HandCrank extends Block {
    public HandCrank() {
        super(Properties.of(Material.WOOD));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return defaultBlockState().setValue(BlockStateProperties.FACING, context.getClickedFace().getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader block, BlockPos pos, ISelectionContext context) {
        switch (state.getValue(BlockStateProperties.FACING)) {
            case NORTH: return VoxelShapes.or(Block.box(7, 7, 0, 9, 9, 3), Block.box(1, 1, 3, 15, 15, 6));
            case SOUTH: return VoxelShapes.or(Block.box(7, 7, 16, 9, 9, 13), Block.box(1, 1, 13, 15, 15, 10));
            case WEST: return VoxelShapes.or(Block.box(0, 7, 7, 3, 9, 9), Block.box(3, 1, 1, 6, 15, 15));
            case EAST: return VoxelShapes.or(Block.box(16, 7, 7, 13, 9, 9), Block.box(13, 1, 1, 10, 15, 15));
            case DOWN: return VoxelShapes.or(Block.box(7, 0, 7, 9, 3, 9), Block.box(1, 3, 1, 15, 6, 15));
            case UP: return VoxelShapes.or(Block.box(7, 16, 7, 9, 13, 9), Block.box(1, 13, 1, 15, 10, 15));
            default: return VoxelShapes.block();
        }
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new HandCrankTile();
    }
}
