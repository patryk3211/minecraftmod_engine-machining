package com.enginemachining.tileentities;

import com.enginemachining.EngineMachiningMod;
import com.enginemachining.blocks.ModdedBlocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModdedTileEntities {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, EngineMachiningMod.MOD_ID);

    public static final RegistryObject<TileEntityType<CrusherTile>> crusher = TILE_ENTITIES.register("crusher", () -> TileEntityType.Builder.of(CrusherTile::new, ModdedBlocks.crusher.get()).build(null));

    public static final RegistryObject<TileEntityType<EnergyWireTile>> energy_wire = TILE_ENTITIES.register("energy_wire", () -> TileEntityType.Builder.of(EnergyWireTile::new, ModdedBlocks.energy_wire_copper.get()).build(null));
    public static final RegistryObject<TileEntityType<PowerLimiterTile>> power_limiter = TILE_ENTITIES.register("power_limiter", () -> TileEntityType.Builder.of(PowerLimiterTile::new, ModdedBlocks.power_limiter_copper.get()).build(null));

    public static final RegistryObject<TileEntityType<InfinitePowerSourceTile>> infinite_power_source = TILE_ENTITIES.register("infinite_power_source", () -> TileEntityType.Builder.of(InfinitePowerSourceTile::new, ModdedBlocks.infinite_power_source.get()).build(null));
}
