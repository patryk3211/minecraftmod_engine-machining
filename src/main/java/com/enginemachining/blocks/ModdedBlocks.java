package com.enginemachining.blocks;

import com.enginemachining.EngineMachiningMod;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModdedBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, EngineMachiningMod.MOD_ID);

    public static final RegistryObject<Block> ore_copper = BLOCKS.register("ore_copper", OreCopper::new);
    public static final RegistryObject<Block> ore_tin = BLOCKS.register("ore_tin", OreTin::new);
    public static final RegistryObject<Block> ore_aluminium = BLOCKS.register("ore_aluminium", OreAluminium::new);
    public static final RegistryObject<Block> ore_silver = BLOCKS.register("ore_silver", OreSilver::new);
    public static final RegistryObject<Block> ore_lead = BLOCKS.register("ore_lead", OreLead::new);
    public static final RegistryObject<Block> ore_nickel = BLOCKS.register("ore_nickel", OreNickel::new);

    public static final RegistryObject<Block> crusher = BLOCKS.register("crusher", Crusher::new);

    public static final RegistryObject<Block> energy_wire_copper = BLOCKS.register("energy_wire_copper", EnergyWireCopper::new);

    public static final RegistryObject<Block> power_limiter_copper = BLOCKS.register("power_limiter_copper", PowerLimiterCopper::new);

    public static final RegistryObject<Block> kinetic_generator = BLOCKS.register("kinetic_generator", KineticGenerator::new);
    public static final RegistryObject<Block> infinite_power_source = BLOCKS.register("infinite_power_source", InfinitePowerSource::new);
}
