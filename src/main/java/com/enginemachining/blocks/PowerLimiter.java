package com.enginemachining.blocks;

import com.enginemachining.capabilities.ModdedCapabilities;
import com.enginemachining.tileentities.CrusherTile;
import com.enginemachining.tileentities.PowerLimiterTile;
import com.enginemachining.utils.EnergyNetwork;
import com.enginemachining.utils.IPipeTraceable;
import com.enginemachining.utils.PipeNetwork;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
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
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
            Block.box(7, 9,10, 9, 11, 11));
    private static final VoxelShape DOWN = VoxelShapes.or(Block.box(7, 0,7, 9, 3, 9),
            Block.box(6, 3,6, 10, 13, 10),
            Block.box(7, 13,7, 9, 16, 9),
            Block.box(7, 5,5, 9, 7, 6));

    private int maxPowerLimit;

    public PowerLimiter(int maxPowerLimit) {
        super(Properties.of(Material.METAL)
            .harvestLevel(1)
            .harvestTool(ToolType.PICKAXE));
        this.maxPowerLimit = maxPowerLimit;
    }

    public int getMaxPowerLimit() {
        return maxPowerLimit;
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

    @Override
    public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if(worldIn.isClientSide) return;
        if(!(newState.getBlock() instanceof EnergyWire)) {
            TileEntity entity = worldIn.getBlockEntity(pos);
            Map<PipeNetwork, Set<Direction>> networks = new HashMap<>();
            if(entity instanceof IPipeTraceable) {
                for (Direction dir : Direction.values()) {
                    PipeNetwork net = ((IPipeTraceable) entity).getNetwork(dir);
                    if(net != null) {
                        if(networks.containsKey(net)) networks.get(net).add(dir);
                        else {
                            Set<Direction> set = new HashSet<>();
                            set.add(dir);
                            networks.put(net, set);
                        }
                    }
                }
            }
            worldIn.removeBlockEntity(pos);
            // TODO: [19.06.2021] For some reason sometimes a network is left only with a power limiter, find out where it is getting created and don't create it.
            networks.forEach((net, dirs) -> {
                for (Direction dir : dirs) {
                    PipeNetwork.removeTraceable(pos, worldIn, ModdedCapabilities.ENERGY, net, () -> new EnergyNetwork(worldIn), dir);
                }
            });
        }
    }

    @Override
    public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity user, Hand hand, BlockRayTraceResult ray) {
        if(!level.isClientSide) {
            TileEntity te = level.getBlockEntity(pos);
            if(te instanceof PowerLimiterTile) {
                NetworkHooks.openGui((ServerPlayerEntity)user, (PowerLimiterTile)te, pos);
                return ActionResultType.SUCCESS;
            }
        } else {
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.FAIL;
    }
}
