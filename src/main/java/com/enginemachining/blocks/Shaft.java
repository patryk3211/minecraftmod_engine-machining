package com.enginemachining.blocks;

import com.enginemachining.api.rotation.KineticBlockProperties;
import com.enginemachining.api.rotation.RotationalNetwork;
import com.enginemachining.tileentities.ShaftTile;
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

public class Shaft extends Block {
    private static final VoxelShape Z = Block.box(6.5, 6.5, 0, 9.5, 9.5, 16);
    private static final VoxelShape Y = Block.box(6.5, 0, 6.5, 9.5, 16, 9.5);
    private static final VoxelShape X = Block.box(0, 6.5, 6.5, 16, 9.5, 9.5);

    public Shaft() {
        super(Properties.of(Material.METAL));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.AXIS);
        builder.add(KineticBlockProperties.MODEL_TYPE);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return defaultBlockState().setValue(BlockStateProperties.AXIS, context.getClickedFace().getAxis()).setValue(KineticBlockProperties.MODEL_TYPE, KineticBlockProperties.ModelType.BODY);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ShaftTile();
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        switch (state.getValue(BlockStateProperties.AXIS)) {
            case Z: return Z;
            case Y: return Y;
            case X: return X;
            default: return VoxelShapes.block();
        }
    }

    @Override
    public void onRemove(BlockState oldState, World level, BlockPos pos, BlockState newState, boolean isMoving) {
        if(level.isClientSide) return;
        if(!(newState.getBlock() instanceof Shaft)) {
            RotationalNetwork.removeFromNetwork(pos, level);
            level.removeBlockEntity(pos);
        }
    }
}
