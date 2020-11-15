package com.enginemachining;

import com.enginemachining.Armour.BootsCopper;
import com.enginemachining.Armour.ChestplateCopper;
import com.enginemachining.Armour.HelmetCopper;
import com.enginemachining.Armour.LegginsCopper;
import com.enginemachining.blocks.*;
import com.enginemachining.containers.CrusherContainer;
import com.enginemachining.containers.ModdedContainers;
import com.enginemachining.items.*;
import com.enginemachining.items.Nuggetsy.*;
import com.enginemachining.items.dust.*;
import com.enginemachining.items.ingot.*;
import com.enginemachining.recipes.ModdedRecipeSerializers;
import com.enginemachining.screens.CrusherScreen;
import com.enginemachining.tileentities.CrusherTile;
import com.enginemachining.tools.AxeCopper;
import com.enginemachining.tools.HoeCopper;
import com.enginemachining.tools.PickaxeCopper;
import com.enginemachining.tools.SwordCopper;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.fml.network.NetworkRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("enginemachining")
public class EngineMachiningMod
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public EngineMachiningMod() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        /*// Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);*/

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        OreGeneration.SetupFeatureGeneration();


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
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        //LOGGER.info("HELLO from server starting");
    }*/

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            LOGGER.info("Starting Block Registry...");
            blockRegistryEvent.getRegistry().register(new OreCopper());
            blockRegistryEvent.getRegistry().register(new OreTin());
            blockRegistryEvent.getRegistry().register(new OreAluminium());
            blockRegistryEvent.getRegistry().register(new OreSilver());
            blockRegistryEvent.getRegistry().register(new OreLead());
            blockRegistryEvent.getRegistry().register(new OreNickel());

            blockRegistryEvent.getRegistry().register(new Crusher());
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

            itemsRegistryEvent.getRegistry().register(new DustCopper());
            itemsRegistryEvent.getRegistry().register(new DustAluminium());
            itemsRegistryEvent.getRegistry().register(new DustTin());
            itemsRegistryEvent.getRegistry().register(new DustNickel());
            itemsRegistryEvent.getRegistry().register(new DustLead());
            itemsRegistryEvent.getRegistry().register(new DustSilver());

            itemsRegistryEvent.getRegistry().register(new NuggetAluminium());
            itemsRegistryEvent.getRegistry().register(new NuggetCopper());
            itemsRegistryEvent.getRegistry().register(new NuggetLead());
            itemsRegistryEvent.getRegistry().register(new NuggetNickel());
            itemsRegistryEvent.getRegistry().register(new NuggetTin());
            itemsRegistryEvent.getRegistry().register(new NuggetSilver());

            itemsRegistryEvent.getRegistry().register(new BlockItem(ModdedBlocks.crusher, new Item.Properties().group(ItemGroup.MISC)).setRegistryName("enginemachining:crusher"));

            itemsRegistryEvent.getRegistry().register(new AxeCopper());
            itemsRegistryEvent.getRegistry().register(new PickaxeCopper());
            itemsRegistryEvent.getRegistry().register(new SwordCopper());
            itemsRegistryEvent.getRegistry().register(new HoeCopper());
            itemsRegistryEvent.getRegistry().register(new HelmetCopper());
            itemsRegistryEvent.getRegistry().register(new ChestplateCopper());
            itemsRegistryEvent.getRegistry().register(new LegginsCopper());
            itemsRegistryEvent.getRegistry().register(new BootsCopper());

            itemsRegistryEvent.getRegistry().register(new BatteryDisposable());

            ModdedItemTags.InitTags();

            ModdedItemGroups.InitItemGroups();
            LOGGER.info("Item registry finished!");
        }

        @SubscribeEvent
        public static void onTileRegistry(RegistryEvent.Register<TileEntityType<?>> tileEntityRegistry) {
            tileEntityRegistry.getRegistry().register(TileEntityType.Builder.create(CrusherTile::new, ModdedBlocks.crusher).build(null).setRegistryName("enginemachining:crusher"));
        }

        @SubscribeEvent
        public static void onContainerRegistry(RegistryEvent.Register<ContainerType<?>> containerRegistry) {
            containerRegistry.getRegistry().register(IForgeContainerType.create(CrusherContainer::new).setRegistryName("enginemachining:crusher"));
        }

        @SubscribeEvent
        public static void onRecipeSerializerRegistry(RegistryEvent.Register<IRecipeSerializer<?>> recipeSerializerRegistry) {
            recipeSerializerRegistry.getRegistry().register(ModdedRecipeSerializers.crusherRecipeSerializer.setRegistryName(new ResourceLocation("enginemachining:crushing")));
        }
    }
}
