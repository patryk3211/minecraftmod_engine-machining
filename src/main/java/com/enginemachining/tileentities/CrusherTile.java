package com.enginemachining.tileentities;

import com.enginemachining.blocks.ModdedBlocks;
import com.enginemachining.containers.CrusherContainer;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraftforge.fml.network.IContainerFactory;

import javax.annotation.Nullable;
import java.util.List;

public class CrusherTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {
    private CrusherContainer container;

    public CrusherTile() {
        super(ModdedTileEntities.crusher);
    }

    @Override
    public void tick() {
        if (!world.isRemote) {
            //System.out.println("Tick");
        }
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("Crusher");
    }

    @Nullable
    @Override
    public Container createMenu(int windowId, PlayerInventory inv, PlayerEntity player) {
        return new CrusherContainer(windowId, inv, this);
    }
}
