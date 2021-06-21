package com.enginemachining.containers;

import com.enginemachining.EngineMachiningMod;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

public class ModdedContainers {
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, EngineMachiningMod.MOD_ID);

    public static final RegistryObject<ContainerType<CrusherContainer>> crusher = CONTAINERS.register("crusher", () -> IForgeContainerType.create(CrusherContainer::new));

    public static final RegistryObject<ContainerType<PowerLimiterContainer>> power_limiter = CONTAINERS.register("power_limiter", () -> IForgeContainerType.create(PowerLimiterContainer::new));
}
