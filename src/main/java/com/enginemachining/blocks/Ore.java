package com.enginemachining.blocks;

import net.minecraft.block.Block;

public enum Ore {
    COPPER(ModdedBlocks.ore_copper.get(), 0, 0, 60, 16, 8),
    TIN(ModdedBlocks.ore_tin.get(), 0, 0, 60, 14, 8),
    SILVER(ModdedBlocks.ore_silver.get(), 0, 0, 25, 10, 6),
    LEAD(ModdedBlocks.ore_lead.get(), 0, 0, 30, 10, 8),
    ALUMINIUM(ModdedBlocks.ore_aluminium.get(), 0, 0, 40, 16, 10),
    NICKEL(ModdedBlocks.ore_nickel.get(), 0, 0, 40, 12, 8);

    private final Block block;
    private final int bottomOffset;
    private final int topOffset;
    private final int perChunk;
    private final int maxVeinSize;
    private final int maxHeight;

    Ore(Block block, int bottomOffset, int topOffset, int maxHeight, int perChunk, int maxVeinSize) {
        this.block = block;
        this.bottomOffset = bottomOffset;
        this.topOffset = topOffset;
        this.perChunk = perChunk;
        this.maxVeinSize = maxVeinSize;
        this.maxHeight = maxHeight;
    }

    public Block getBlock() {
        return block;
    }

    public int getBottomOffset() {
        return bottomOffset;
    }

    public int getTopOffset() {
        return topOffset;
    }

    public int getPerChunk() {
        return perChunk;
    }

    public int getMaxVeinSize() {
        return maxVeinSize;
    }

    public int getMaxHeight() {
        return maxHeight;
    }
}
