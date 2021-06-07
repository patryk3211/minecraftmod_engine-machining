package com.enginemachining.blocks;

import com.enginemachining.tileentities.PowerLimiterTile;
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
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public abstract class PowerLimiter extends Block {
    private static final VoxelShape NORTH = VoxelShapes.or(Block.box(7, 7,0, 9, 9, 3),
            Block.box(6, 6,3, 10, 10, 13),
            Block.box(7, 7,13, 9, 9, 16),
            Block.box(7, 10,5, 9, 11, 7));
    private static final VoxelShape SOUTH = VoxelShapes.or(Block.box(7, 7,0, 9, 9, 3),
            Block.box(6, 6,3, 10, 10, 13),
            Block.box(7, 7,13, 9, 9, 16),
            Block.box(7, 10,9, 9, 11, 11));
    private static final VoxelShape EAST = VoxelShapes.or(Block.box(0, 7,7, 3, 9, 9),
            Block.box(3, 6,6, 13, 10, 10),
            Block.box(13, 7,7, 16, 9, 9),
            Block.box(9, 10,7, 11, 11, 9));
    private static final VoxelShape WEST = VoxelShapes.or(Block.box(0, 7,7, 3, 9, 9),
            Block.box(3, 6,6, 13, 10, 10),
            Block.box(13, 7,7, 16, 9, 9),
            Block.box(5, 10,7, 7, 11, 9));
    private static final VoxelShape UP = VoxelShapes.or(Block.box(7, 0,7, 9, 3, 9),
            Block.box(6, 3,6, 10, 13, 10),
            Block.box(7, 13,7, 9, 16, 9),
            Block.box(7, 5,5, 9, 7, 6));
    private static final VoxelShape DOWN = VoxelShapes.or(Block.box(7, 0,7, 9, 3, 9),
            Block.box(6, 3,6, 10, 13, 10),
            Block.box(7, 13,7, 9, 16, 9),
            Block.box(7, 9,10, 9, 11, 11));

    private int maxPowerLimit;

    public PowerLimiter(int maxPowerLimit) {
        super(Properties.of(Material.METAL)
            .harvestLevel(1)
            .harvestTool(ToolType.PICKAXE));
        this.maxPowerLimit = maxPowerLimit;
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
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        switch(state.getValue(BlockStateProperties.FACING)) {
            case NORTH: return NORTH;
            case SOUTH: return SOUTH;
            case EAST: return EAST;
            case WEST: return WEST;
            case UP: return UP;
            case DOWN: return DOWN;
            default: return null;
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new PowerLimiterTile();
    }
}
