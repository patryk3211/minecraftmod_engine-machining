package com.enginemachining;

import com.enginemachining.blocks.*;
import com.enginemachining.items.*;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("enginemachining")
public class EngineMachiningMod
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public EngineMachiningMod() {
        // Register the setup method for modloading
        /*FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);*/

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    /*private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }*/
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        //LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("Starting Block Registry...");
            blockRegistryEvent.getRegistry().register(new OreCopper());
            blockRegistryEvent.getRegistry().register(new OreTin());
            blockRegistryEvent.getRegistry().register(new OreAluminium());
            blockRegistryEvent.getRegistry().register(new OreSilver());
            blockRegistryEvent.getRegistry().register(new OreLead());
            blockRegistryEvent.getRegistry().register(new OreNickel());
            LOGGER.info("Block registry finished!");
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> itemsRegistryEvent) {
            LOGGER.info("Starting Item Registry...");
            itemsRegistryEvent.getRegistry().register(new BlockItem(ModdedBlocks.ore_copper, new Item.Properties().group(ModdedItemGroups.metals)).setRegistryName("enginemachining:ore_copper"));
            itemsRegistryEvent.getRegistry().register(new BlockItem(ModdedBlocks.ore_tin, new Item.Properties().group(ModdedItemGroups.metals)).setRegistryName("enginemachining:ore_tin"));
            itemsRegistryEvent.getRegistry().register(new BlockItem(ModdedBlocks.ore_aluminium, new Item.Properties().group(ModdedItemGroups.metals)).setRegistryName("enginemachining:ore_aluminium"));
            itemsRegistryEvent.getRegistry().register(new BlockItem(ModdedBlocks.ore_silver, new Item.Properties().group(ModdedItemGroups.metals)).setRegistryName("enginemachining:ore_silver"));
            itemsRegistryEvent.getRegistry().register(new BlockItem(ModdedBlocks.ore_lead, new Item.Properties().group(ModdedItemGroups.metals)).setRegistryName("enginemachining:ore_lead"));
            itemsRegistryEvent.getRegistry().register(new BlockItem(ModdedBlocks.ore_nickel, new Item.Properties().group(ModdedItemGroups.metals)).setRegistryName("enginemachining:ore_nickel"));

            itemsRegistryEvent.getRegistry().register(new IngotCopper());
            itemsRegistryEvent.getRegistry().register(new IngotAluminium());
            itemsRegistryEvent.getRegistry().register(new IngotTin());
            itemsRegistryEvent.getRegistry().register(new IngotNickel());
            itemsRegistryEvent.getRegistry().register(new IngotLead());
            itemsRegistryEvent.getRegistry().register(new IngotSilver());
            LOGGER.info("Item registry finished!");
        }
    }
}
