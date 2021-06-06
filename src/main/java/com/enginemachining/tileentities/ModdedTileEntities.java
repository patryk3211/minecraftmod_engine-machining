package com.enginemachining.tileentities;

import com.enginemachining.EngineMachiningMod;
import com.enginemachining.blocks.Crusher;
import com.enginemachining.blocks.EnergyWire;
import com.enginemachining.blocks.ModdedBlocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

import java.util.function.Supplier;

public class ModdedTileEntities {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, EngineMachiningMod.MOD_ID);

    public static final RegistryObject<TileEntityType<CrusherTile>> crusher = TILE_ENTITIES.register("crusher", () -> TileEntityType.Builder.create(CrusherTile::new, ModdedBlocks.crusher.get()).build(null));
    public static final RegistryObject<TileEntityType<EnergyWireTile>> energy_wire = TILE_ENTITIES.register("energy_wire", () -> TileEntityType.Builder.create(EnergyWireTile::new, ModdedBlocks.energy_wire_copper.get()).build(null));

    public static final RegistryObject<TileEntityType<DebugEnergySourceTile>> debug_energy_source = TILE_ENTITIES.register("debug_energy_source", () -> TileEntityType.Builder.create(DebugEnergySourceTile::new, ModdedBlocks.debug_energy_source.get()).build(null));
}
