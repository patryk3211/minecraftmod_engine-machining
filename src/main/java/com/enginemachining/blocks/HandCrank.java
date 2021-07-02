package com.enginemachining.blocks;

import com.enginemachining.api.rotation.KineticBlockProperties;
import com.enginemachining.api.rotation.RotationalNetwork;
import com.enginemachining.tileentities.HandCrankTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class HandCrank extends Block {
    private static final VoxelShape NORTH = VoxelShapes.or(Block.box(6.5, 6.5, 0, 9.5, 9.5, 3), Block.box(1, 1, 3, 15, 15, 6)).optimize();
    private static final VoxelShape SOUTH = VoxelShapes.or(Block.box(6.5, 6.5, 16, 9.5, 9.5, 13), Block.box(1, 1, 13, 15, 15, 10)).optimize();
    private static final VoxelShape WEST = VoxelShapes.or(Block.box(0, 6.5, 6.5, 3, 9.5, 9.5), Block.box(3, 1, 1, 6, 15, 15));
    private static final VoxelShape EAST = VoxelShapes.or(Block.box(16, 6.5, 6.5, 13, 9.5, 9.5), Block.box(13, 1, 1, 10, 15, 15));
    private static final VoxelShape DOWN = VoxelShapes.or(Block.box(6.5, 0, 6.5, 9.5, 3, 9.5), Block.box(1, 3, 1, 15, 6, 15));
    private static final VoxelShape UP = VoxelShapes.or(Block.box(6.5, 16, 6.5, 9.5, 13, 9.5), Block.box(1, 13, 1, 15, 10, 15));


    public HandCrank() {
        super(Properties.of(Material.WOOD));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING);
        builder.add(KineticBlockProperties.MODEL_TYPE);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return defaultBlockState().setValue(BlockStateProperties.FACING, context.getClickedFace().getOpposite()).setValue(KineticBlockProperties.MODEL_TYPE, KineticBlockProperties.ModelType.BODY);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader block, BlockPos pos, ISelectionContext context) {
        switch (state.getValue(BlockStateProperties.FACING)) {
            case NORTH: return NORTH;
            case SOUTH: return SOUTH;
            case WEST: return WEST;
            case EAST: return EAST;
            case DOWN: return DOWN;
            case UP: return UP;
            default: return VoxelShapes.block();
        }
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

    @Override
    public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity user, Hand hand, BlockRayTraceResult ray) {
        if(!level.isClientSide) {
            TileEntity te = level.getBlockEntity(pos);
            if(!(te instanceof HandCrankTile)) return ActionResultType.FAIL;
            HandCrankTile hct = (HandCrankTile) te;
            RotationalNetwork net = (RotationalNetwork) hct.getNetwork();
            float force = net.calculateForceForSpeed(1.5f);
            force = Math.min(force, 10f);
            net.applyTickForce(force);
            user.causeFoodExhaustion(force/20f);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.CONSUME;
    }

    @Override
    public void onRemove(BlockState state, World level, BlockPos pos, BlockState newState, boolean isMoving) {
        if(level.isClientSide) return;
        if(!(newState.getBlock() instanceof HandCrank)) {
            RotationalNetwork.removeFromNetwork(pos, level);
            level.removeBlockEntity(pos);
        }
    }
}
