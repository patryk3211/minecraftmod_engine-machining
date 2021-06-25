package com.enginemachining.api.rotation;

import net.minecraft.state.EnumProperty;
import net.minecraft.util.IStringSerializable;

public class KineticBlockProperties {
    public enum ModelType implements IStringSerializable {
        /**
         * Static part of the block
         */
        BODY("body"),
        /**
         * Rotating part of the block
         */
        SHAFT("shaft");

        private String str;

        ModelType(String str) {
            this.str = str;
        }

        @Override
        public String getSerializedName() {
            return str;
        }
    }

    public static final EnumProperty<ModelType> MODEL_TYPE = EnumProperty.create("model_type", ModelType.class);
}
