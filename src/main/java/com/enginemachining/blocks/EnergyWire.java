package com.enginemachining.blocks;

import com.enginemachining.handlers.IEnergyReceiver;
import com.enginemachining.handlers.IEnergySender;
import com.enginemachining.tileentities.EnergyWireTile;
import com.enginemachining.utils.DirectionTools;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import org.lwjgl.system.CallbackI;

import javax.annotation.Nullable;

public abstract class EnergyWire extends Block {
    int maxFEPerTick;

    public EnergyWire(int maxFEPerTick) {
        super(Properties.create(Material.IRON)
                .harvestLevel(1)
                .harvestTool(ToolType.PICKAXE)
                .setRequiresTool()
                );
        setDefaultState(getDefaultState()
                .with(BlockStateProperties.NORTH, false)
                .with(BlockStateProperties.SOUTH, false)
                .with(BlockStateProperties.EAST, false)
                .with(BlockStateProperties.WEST, false)
                .with(BlockStateProperties.UP, false)
                .with(BlockStateProperties.DOWN, false));
        this.maxFEPerTick = maxFEPerTick;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.NORTH);
        builder.add(BlockStateProperties.SOUTH);
        builder.add(BlockStateProperties.EAST);
        builder.add(BlockStateProperties.WEST);
        builder.add(BlockStateProperties.UP);
        builder.add(BlockStateProperties.DOWN);
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        //Change block state to maybe connect to another wire
        BlockPos deltaPos = fromPos.subtract(pos);
        Direction neighborDir = Direction.getFacingFromVector(deltaPos.getX(), deltaPos.getY(), deltaPos.getZ());
        TileEntity neighbor = worldIn.getTileEntity(fromPos);
        boolean blockConnectable = neighbor instanceof EnergyWireTile ||
                (neighbor != null && neighbor.getCapability(CapabilityEnergy.ENERGY, neighborDir).isPresent());
        TileEntity te = worldIn.getTileEntity(pos);
        if(blockConnectable && te instanceof EnergyWireTile && neighbor instanceof EnergyWireTile) {
            blockConnectable = ((EnergyWireTile) te).isSideConnectable(neighborDir) && ((EnergyWireTile) neighbor).isSideConnectable(neighborDir.getOpposite());
        }
        worldIn.setBlockState(pos, state.with(DirectionTools.DirectionToProperty(neighborDir), blockConnectable));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        //Check if any neighbors are wires, if so connect to them
        BlockState state = getDefaultState();
        TileEntity te = context.getWorld().getTileEntity(context.getPos());
        EnergyWireTile ewt = null;
        if(te instanceof EnergyWireTile) ewt = (EnergyWireTile) te;
        for(Direction dir : Direction.values()) {
            BlockPos neighborPos = context.getPos().add(dir.getDirectionVec());
            TileEntity neighbor = context.getWorld().getTileEntity(neighborPos);
            boolean blockConnectable = neighbor instanceof EnergyWireTile ||
                    (neighbor != null && neighbor.getCapability(CapabilityEnergy.ENERGY, dir).isPresent());
            if(neighbor instanceof EnergyWireTile) {
                blockConnectable = ((EnergyWireTile) neighbor).isSideConnectable(dir.getOpposite());
            }
            state = state.with(DirectionTools.DirectionToProperty(dir), blockConnectable);
        }
        return state;
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) { worldIn.removeTileEntity(pos); }

    @Override
    public boolean hasTileEntity(BlockState state) { return true; }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) { return new EnergyWireTile(); }

    @Override
    public abstract VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context);

    public int getMaxFEPerTick() { return maxFEPerTick; }
}
