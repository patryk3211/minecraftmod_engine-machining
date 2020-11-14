package com.enginemachining.blocks;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

public class ModdedBlocks {
    @ObjectHolder("enginemachining:ore_copper")
    public static OreCopper ore_copper;
    @ObjectHolder("enginemachining:ore_tin")
    public static OreTin ore_tin;
    @ObjectHolder("enginemachining:ore_aluminium")
    public static OreAluminium ore_aluminium;
    @ObjectHolder("enginemachining:ore_silver")
    public static OreSilver ore_silver;
    @ObjectHolder("enginemachining:ore_lead")
    public static OreLead ore_lead;
    @ObjectHolder("enginemachining:ore_nickel")
    public static OreNickel ore_nickel;

    @ObjectHolder("enginemachining:crusher")
    public static Crusher crusher;

    @ObjectHolder("enginemachining:crusher")
    public static TileEntityType<?> crusher_tile;
}
