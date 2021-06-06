package com.enginemachining.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public abstract class PowerLimiter extends Block {
    private static final VoxelShape SHAPE = VoxelShapes.or(Block.box(7, 7,0, 9, 9, 3),
                                                           Block.box(6, 6,3, 10, 10, 13),
                                                           Block.box(7, 7,13, 9, 9, 16),
                                                           Block.box(7, 10,5, 9, 11, 7));

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
        BlockState state = defaultBlockState();
        state.setValue(BlockStateProperties.FACING, context.getClickedFace().getOpposite());
        return state;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }
}
