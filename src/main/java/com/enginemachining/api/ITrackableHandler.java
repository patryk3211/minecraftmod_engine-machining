package com.enginemachining.api;

import net.minecraft.util.Direction;

import javax.annotation.Nullable;

public interface ITrackableHandler {
    /**
     * Specifies whenever a side can be used to extract power
     * @param dir Side to check
     * @return Can it be used for extraction
     */
    boolean canSideExtract(@Nullable Direction dir);

    /**
     * Specifies whenever a side can be used to insert power
     * @param dir Side to check
     * @return Can it be used for insertion
     */
    boolean canSideInsert(@Nullable Direction dir);
}
