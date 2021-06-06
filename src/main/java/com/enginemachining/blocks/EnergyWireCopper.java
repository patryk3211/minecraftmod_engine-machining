package com.enginemachining.blocks;

import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.*;
import net.minecraft.world.IBlockReader;

public class EnergyWireCopper extends EnergyWire {
    public static final VoxelShape center = Block.makeCuboidShape(7, 7, 7,9, 9, 9);
    public static final VoxelShape east = Block.makeCuboidShape(9, 7, 7, 16, 9, 9);
    public static final VoxelShape west = Block.makeCuboidShape(7, 7, 7, 0, 9, 9);
    public static final VoxelShape up = Block.makeCuboidShape(7, 9, 7, 9, 16, 9);
    public static final VoxelShape down = Block.makeCuboidShape(7, 7, 7, 9, 0, 9);
    public static final VoxelShape south = Block.makeCuboidShape(7, 7, 9, 9, 9, 16);
    public static final VoxelShape north = Block.makeCuboidShape(7, 7, 7, 9, 9, 0);

    public EnergyWireCopper() {
        super(1000);

    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        VoxelShape ret = center;
        if(state.get(BlockStateProperties.EAST)) ret = VoxelShapes.combine(ret, east, IBooleanFunction.OR);
        if(state.get(BlockStateProperties.WEST)) ret = VoxelShapes.combine(ret, west, IBooleanFunction.OR);
        if(state.get(BlockStateProperties.DOWN)) ret = VoxelShapes.combine(ret, down, IBooleanFunction.OR);
        if(state.get(BlockStateProperties.UP)) ret = VoxelShapes.combine(ret, up, IBooleanFunction.OR);
        if(state.get(BlockStateProperties.NORTH)) ret = VoxelShapes.combine(ret, north, IBooleanFunction.OR);
        if(state.get(BlockStateProperties.SOUTH)) ret = VoxelShapes.combine(ret, south, IBooleanFunction.OR);

        return ret.simplify();
    }
}
