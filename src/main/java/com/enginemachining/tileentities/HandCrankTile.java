package com.enginemachining.tileentities;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

public class HandCrankTile extends TileEntity implements ITickableTileEntity {
    public float velocity;

    public double angle;
    public double toRotate;

    public HandCrankTile() {
        super(ModdedTileEntities.handcrank.get());

        velocity = 1;
        angle = 0;
        toRotate = 0;
    }


    @Override
    public void tick() {
        velocity = 360f;
        if(level.isClientSide) {
            toRotate = velocity * (1/20f);
        }
    }
}
