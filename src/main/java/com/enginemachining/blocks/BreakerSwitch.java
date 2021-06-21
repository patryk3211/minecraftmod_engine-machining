package com.enginemachining.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public abstract class BreakerSwitch extends Block {
    private static final VoxelShape NORTH = VoxelShapes.or(Block.box(7, 7, 0, 9, 9, 2),
            Block.box(7, 7, 14, 9, 9, 16),
            Block.box(4, 6, 2, 12, 11, 14)).optimize();

    public BreakerSwitch() {
        super(Properties.of(Material.METAL));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.ENABLED);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return defaultBlockState().setValue(BlockStateProperties.ENABLED, false);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader block, BlockPos pos, ISelectionContext context) {
        return NORTH;
    }

    @Override
    public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity user, Hand hand, BlockRayTraceResult ray) {
        if(!level.isClientSide && hand == Hand.MAIN_HAND) {
            level.setBlock(pos, state.setValue(BlockStateProperties.ENABLED, !state.getValue(BlockStateProperties.ENABLED)), Constants.BlockFlags.BLOCK_UPDATE);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }
}
