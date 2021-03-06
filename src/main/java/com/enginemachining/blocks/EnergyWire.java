package com.enginemachining.blocks;

import com.enginemachining.capabilities.ModdedCapabilities;
import com.enginemachining.tileentities.EnergyWireTile;
import com.enginemachining.utils.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
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
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class EnergyWire extends Block {
    int maxFEPerTick;

    public EnergyWire(int maxFEPerTick) {
        super(Properties.of(Material.METAL)
                .harvestLevel(1)
                .harvestTool(ToolType.PICKAXE));
        defaultBlockState().setValue(BlockStateProperties.NORTH, false)
                .setValue(BlockStateProperties.SOUTH, false)
                .setValue(BlockStateProperties.EAST, false)
                .setValue(BlockStateProperties.WEST, false)
                .setValue(BlockStateProperties.UP, false)
                .setValue(BlockStateProperties.DOWN, false);
        this.maxFEPerTick = maxFEPerTick;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
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
        Direction neighborDir = Direction.getNearest(deltaPos.getX(), deltaPos.getY(), deltaPos.getZ());
        TileEntity neighbor = worldIn.getBlockEntity(fromPos);
        boolean blockConnectable = neighbor instanceof EnergyWireTile ||
                (neighbor != null && neighbor.getCapability(ModdedCapabilities.ENERGY, neighborDir.getOpposite()).isPresent());
        TileEntity te = worldIn.getBlockEntity(pos);
        if(blockConnectable && te instanceof EnergyWireTile && neighbor instanceof EnergyWireTile) {
            blockConnectable = ((EnergyWireTile) te).isSideConnectable(neighborDir) && ((EnergyWireTile) neighbor).isSideConnectable(neighborDir.getOpposite());
        }
        // When setting the block a new tile entity is created
        worldIn.setBlock(pos, state.setValue(DirectionTools.DirectionToProperty(neighborDir), blockConnectable), Constants.BlockFlags.DEFAULT);
    }

    public BlockState createState(BlockPos pos, World level) {
        BlockState state = defaultBlockState();
        TileEntity te = level.getBlockEntity(pos);
        EnergyWireTile ewt = null;
        if(te instanceof EnergyWireTile) ewt = (EnergyWireTile) te;
        for(Direction dir : Direction.values()) {
            BlockPos neighborPos = pos.offset(dir.getNormal());
            TileEntity neighbor = level.getBlockEntity(neighborPos);
            boolean blockConnectable = false;
            if(ewt == null || ewt.isSideConnectable(dir)) {
                blockConnectable = neighbor instanceof EnergyWireTile ||
                        (neighbor != null && neighbor.getCapability(ModdedCapabilities.ENERGY, dir.getOpposite()).isPresent());
                if (neighbor instanceof EnergyWireTile) {
                    blockConnectable = ((EnergyWireTile) neighbor).isSideConnectable(dir.getOpposite());
                }
            }
            state = state.setValue(DirectionTools.DirectionToProperty(dir), blockConnectable);
        }
        return state;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return defaultBlockState();
    }

    @Override
    public void setPlacedBy(World level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        level.setBlock(pos, createState(pos, level), Constants.BlockFlags.BLOCK_UPDATE);
    }

    @Override
    public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if(worldIn.isClientSide) return;
        if(!(newState.getBlock() instanceof EnergyWire)) {
            TileEntity entity = worldIn.getBlockEntity(pos);
            PipeNetwork network = null;
            if(entity instanceof IPipeTraceable) network = ((IPipeTraceable) entity).getNetwork(null);
            worldIn.removeBlockEntity(pos);
            PipeNetwork.removeTraceable(pos, worldIn, ModdedCapabilities.ENERGY, network, () -> new EnergyNetwork(worldIn), null);
        }
    }

    @Override
    public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity user, Hand hand, BlockRayTraceResult ray) {
        if(!level.isClientSide && hand == Hand.MAIN_HAND) {
            TileEntity te = level.getBlockEntity(pos);
            float powerFlow = 0;
            if(te instanceof EnergyWireTile) {
                powerFlow = ((EnergyWireTile) te).getPowerFlow();
            }
            user.sendMessage(new StringTextComponent("Power Flow: " + powerFlow + "/" + maxFEPerTick), UUID.fromString("00000000-0000-0000-0000-000000000000"));
        }
        return ActionResultType.PASS;
    }

    @Override
    public boolean hasTileEntity(BlockState state) { return true; }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) { return new EnergyWireTile(); }

    @Override
    public abstract VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context);

    public int getMaxFEPerTick() { return maxFEPerTick; }
}
