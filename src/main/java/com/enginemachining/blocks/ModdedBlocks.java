package com.enginemachining.blocks;

import com.enginemachining.EngineMachiningMod;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

public class ModdedBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, EngineMachiningMod.MOD_ID);

    public static final RegistryObject<Block> ore_copper = BLOCKS.register("ore_copper", OreCopper::new);
    public static final RegistryObject<Block> ore_tin = BLOCKS.register("ore_tin", OreTin::new);
    public static final RegistryObject<Block> ore_aluminium = BLOCKS.register("ore_aluminium", OreAluminium::new);
    public static final RegistryObject<Block> ore_silver = BLOCKS.register("ore_silver", OreSilver::new);
    public static final RegistryObject<Block> ore_lead = BLOCKS.register("ore_lead", OreLead::new);
    public static final RegistryObject<Block> ore_nickel = BLOCKS.register("ore_nickel", OreNickel::new);

    public static final RegistryObject<Block> crusher = BLOCKS.register("crusher", Crusher::new);

    /*@ObjectHolder("enginemachining:ore_copper")
    public static OreCopper ore_copper;*/
    /*@ObjectHolder("enginemachining:ore_tin")
    public static OreTin ore_tin;
    @ObjectHolder("enginemachining:ore_aluminium")
    public static OreAluminium ore_aluminium;
    @ObjectHolder("enginemachining:ore_silver")
    public static OreSilver ore_silver;
    @ObjectHolder("enginemachining:ore_lead")
    public static OreLead ore_lead;
    @ObjectHolder("enginemachining:ore_nickel")
    public static OreNickel ore_nickel;*/

    /*@ObjectHolder("enginemachining:crusher")
    public static Crusher crusher;*/
}
