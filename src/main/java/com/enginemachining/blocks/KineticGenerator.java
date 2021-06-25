package com.enginemachining.blocks;

import com.enginemachining.tileentities.KineticGeneratorTile;
import net.minecraft.block.Block;
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

public class KineticGenerator extends Block {
    private static final VoxelShape NORTH = VoxelShapes.or(Block.box(7, 7, 15, 9, 9, 16),
            Block.box(3, 3, 1, 13, 13, 15)).optimize();
    private static final VoxelShape SOUTH = VoxelShapes.or(Block.box(7, 7, 0, 9, 9, 1),
            Block.box(3, 3, 1, 13, 13, 15)).optimize();
    private static final VoxelShape EAST = VoxelShapes.or(Block.box(0, 7, 7, 1, 9, 9),
            Block.box(1, 3, 3, 15, 13, 13)).optimize();
    private static final VoxelShape WEST = VoxelShapes.or(Block.box(15, 7, 7, 16, 9, 9),
            Block.box(1, 3, 3, 15, 13, 13)).optimize();
    private static final VoxelShape UP = VoxelShapes.or(Block.box(7, 0, 7, 9, 1, 9),
            Block.box(3, 1, 3, 13, 15, 13)).optimize();
    private static final VoxelShape DOWN = VoxelShapes.or(Block.box(7, 15, 7, 9, 16, 9),
            Block.box(3, 1, 3, 13, 15, 13)).optimize();

    public KineticGenerator() {
        super(Properties.of(Material.METAL));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader block, BlockPos pos, ISelectionContext context) {
        switch(state.getValue(BlockStateProperties.FACING)) {
            case NORTH: return NORTH;
            case SOUTH: return SOUTH;
            case EAST: return EAST;
            case WEST: return WEST;
            case UP: return UP;
            case DOWN: return DOWN;
            default: return VoxelShapes.block();
        }
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return defaultBlockState().setValue(BlockStateProperties.FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new KineticGeneratorTile();
    }

    @Override
    public void onRemove(BlockState state, World level, BlockPos pos, BlockState newState, boolean isMoving) {
        if(level.isClientSide) return;
        if(!(newState.getBlock() instanceof KineticGenerator)) {
            level.removeBlockEntity(pos);
        }
    }
}
