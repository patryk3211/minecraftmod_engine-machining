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
    public static final VoxelShape center = Block.box(7, 7, 7,9, 9, 9);
    public static final VoxelShape east = Block.box(9, 7, 7, 16, 9, 9);
    public static final VoxelShape west = Block.box(7, 7, 7, 0, 9, 9);
    public static final VoxelShape up = Block.box(7, 9, 7, 9, 16, 9);
    public static final VoxelShape down = Block.box(7, 7, 7, 9, 0, 9);
    public static final VoxelShape south = Block.box(7, 7, 9, 9, 9, 16);
    public static final VoxelShape north = Block.box(7, 7, 7, 9, 9, 0);

    public EnergyWireCopper() {
        super(1000);

    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        VoxelShape ret = center;
        if(state.getValue(BlockStateProperties.EAST)) ret = VoxelShapes.join(ret, east, IBooleanFunction.OR);
        if(state.getValue(BlockStateProperties.WEST)) ret = VoxelShapes.join(ret, west, IBooleanFunction.OR);
        if(state.getValue(BlockStateProperties.DOWN)) ret = VoxelShapes.join(ret, down, IBooleanFunction.OR);
        if(state.getValue(BlockStateProperties.UP)) ret = VoxelShapes.join(ret, up, IBooleanFunction.OR);
        if(state.getValue(BlockStateProperties.NORTH)) ret = VoxelShapes.join(ret, north, IBooleanFunction.OR);
        if(state.getValue(BlockStateProperties.SOUTH)) ret = VoxelShapes.join(ret, south, IBooleanFunction.OR);

        return ret.optimize();
    }
}
