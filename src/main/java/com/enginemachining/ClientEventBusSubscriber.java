package com.enginemachining;

import com.enginemachining.containers.CrusherContainer;
import com.enginemachining.containers.ModdedContainers;
import com.enginemachining.screens.CrusherScreen;
import com.enginemachining.screens.PowerLimiterScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = EngineMachiningMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent clientSetupEvent) {
        ScreenManager.register(ModdedContainers.crusher.get(), CrusherScreen::new);
        ScreenManager.register(ModdedContainers.power_limiter.get(), PowerLimiterScreen::new);
    }
}
