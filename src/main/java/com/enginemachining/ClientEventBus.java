package com.enginemachining;

import com.enginemachining.api.rotation.ClientRotationalNetwork;
import com.enginemachining.containers.ModdedContainers;
import com.enginemachining.renderers.HandCrankRenderer;
import com.enginemachining.renderers.KineticGeneratorRenderer;
import com.enginemachining.renderers.ShaftRenderer;
import com.enginemachining.screens.CrusherScreen;
import com.enginemachining.screens.PowerLimiterScreen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

public class ClientEventBus {
    public static void clientSetup(FMLClientSetupEvent clientSetupEvent) {
        ScreenManager.register(ModdedContainers.crusher.get(), CrusherScreen::new);
        ScreenManager.register(ModdedContainers.power_limiter.get(), PowerLimiterScreen::new);

        MinecraftForge.EVENT_BUS.addListener(ClientRotationalNetwork::onRenderTick);

        HandCrankRenderer.register();
        KineticGeneratorRenderer.register();
        ShaftRenderer.register();
    }

    public static void loadComplete(FMLLoadCompleteEvent loadCompleteEvent) {

    }
}
