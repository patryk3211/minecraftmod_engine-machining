package com.enginemachining;

import com.enginemachining.api.rotation.RotationalNetwork;
import com.enginemachining.blocks.*;
import com.enginemachining.capabilities.ModdedCapabilities;
import com.enginemachining.containers.ModdedContainers;
import com.enginemachining.items.*;
import com.enginemachining.messages.CrusherTileMessage;
import com.enginemachining.messages.PowerLimiterMessage;
import com.enginemachining.messages.RotationalNetworkMessage;
import com.enginemachining.recipes.ModdedRecipeSerializers;
import com.enginemachining.tileentities.ModdedTileEntities;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(EngineMachiningMod.MOD_ID)
public class EngineMachiningMod
{
    public static final String MOD_ID = "enginemachining";

    public static final Logger LOGGER = LogManager.getLogger();

    public EngineMachiningMod() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModdedBlocks.BLOCKS.register(modBus);
        ModdedItems.ITEMS.register(modBus);
        ModdedTileEntities.TILE_ENTITIES.register(modBus);
        ModdedContainers.CONTAINERS.register(modBus);
        ModdedRecipeSerializers.RECIPES.register(modBus);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientEventBus::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientEventBus::loadComplete);

        MinecraftForge.EVENT_BUS.addListener(this::onBiomeLoad);
        RotationalNetwork.registerEvents();

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Registering Network Packets...");
        EngineMachiningPacketHandler.registerPacketType(CrusherTileMessage.class, CrusherTileMessage::encode, CrusherTileMessage::decode, CrusherTileMessage::handle);
        EngineMachiningPacketHandler.registerPacketType(PowerLimiterMessage.class, PowerLimiterMessage::encode, PowerLimiterMessage::decode, PowerLimiterMessage::handle);
        EngineMachiningPacketHandler.registerPacketType(RotationalNetworkMessage.class, RotationalNetworkMessage::encode, RotationalNetworkMessage::decode, RotationalNetworkMessage::handle);
        LOGGER.info("Registering Capabilities...");
        ModdedCapabilities.register();
        LOGGER.info("Setup Complete!");
    }

    private void onBiomeLoad(final BiomeLoadingEvent event) {
        OreGeneration.onBiomeLoad(event);
    }
}
