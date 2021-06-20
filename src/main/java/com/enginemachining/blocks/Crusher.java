package com.enginemachining.blocks;

import com.enginemachining.capabilities.ModdedCapabilities;
import com.enginemachining.tileentities.CrusherTile;
import com.enginemachining.tileentities.EnergyWireTile;
import com.enginemachining.utils.EnergyNetwork;
import com.enginemachining.utils.IPipeTraceable;
import com.enginemachining.utils.PipeNetwork;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class Crusher extends Block {

    public Crusher() {
        super(Properties.of(Material.METAL)
                .strength(5.0f, 3.0f)
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(1));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CrusherTile();
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(!worldIn.isClientSide) {
            TileEntity te = worldIn.getBlockEntity(pos);
            if(te instanceof CrusherTile) {
                NetworkHooks.openGui((ServerPlayerEntity)player, (CrusherTile)te, pos);
                return ActionResultType.SUCCESS;
            }
        } else {
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.FAIL;
    }

    @Override
    public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if(state.getBlock() != newState.getBlock()) {
            TileEntity te = worldIn.getBlockEntity(pos);
            if(te instanceof CrusherTile) {
                InventoryHelper.dropContents(worldIn, pos, ((CrusherTile) te).blockInventory);
                TileEntity entity = worldIn.getBlockEntity(pos);
                PipeNetwork network = null;
                if(entity instanceof IPipeTraceable) network = ((IPipeTraceable) entity).getNetwork(null);
                worldIn.removeBlockEntity(te.getBlockPos());
                PipeNetwork.removeTraceable(pos, worldIn, ModdedCapabilities.ENERGY, network, () -> new EnergyNetwork(worldIn), null);
            }
        }
    }
}
