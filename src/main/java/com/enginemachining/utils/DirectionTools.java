package com.enginemachining.utils;

import net.minecraft.state.BooleanProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;

public class DirectionTools {
    public static BooleanProperty DirectionToProperty(Direction direction) {
        switch(direction) {
            case UP: return BlockStateProperties.UP;
            case DOWN: return BlockStateProperties.DOWN;
            case EAST: return BlockStateProperties.EAST;
            case WEST: return BlockStateProperties.WEST;
            case SOUTH: return BlockStateProperties.SOUTH;
            case NORTH: return BlockStateProperties.NORTH;
            default: return null;
        }
    }
}
