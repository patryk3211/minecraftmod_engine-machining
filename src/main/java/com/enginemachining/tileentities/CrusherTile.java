package com.enginemachining.tileentities;

import com.enginemachining.blocks.ModdedBlocks;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class CrusherTile extends TileEntity implements ITickableTileEntity {
    public CrusherTile() {
        super(ModdedBlocks.crusher_tile);
    }

    @Override
    public void tick() {
        if(!world.isRemote) {
            System.out.println("Tick");
        }
    }
}
