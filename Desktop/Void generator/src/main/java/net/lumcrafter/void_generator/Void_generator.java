package net.lumcrafter.void_generator;

import com.mojang.logging.LogUtils;
import net.lumcrafter.void_generator.block.ModBlocks;
import net.lumcrafter.void_generator.config.ModConfig;
import net.lumcrafter.void_generator.entity.ModBlockEntities;
import net.lumcrafter.void_generator.item.ModCreativeModeTabs;
import net.lumcrafter.void_generator.item.ModItems;
import net.lumcrafter.void_generator.network.ModMessages;
import net.lumcrafter.void_generator.screen.ModMenuTypes;
import net.lumcrafter.void_generator.screen.VoidGenScreen;
import net.lumcrafter.void_generator.sound.ModSounds;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@SuppressWarnings("removal")
@Mod(Void_generator.MODID)
public class Void_generator {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "void_generator";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "void_generator" namespace
    public Void_generator(){
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModCreativeModeTabs.register(modEventBus);
        ModBlocks.register(modEventBus);

        ModItems.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
        ModMenuTypes.register(modEventBus);

        ModSounds.register(modEventBus);
        ModMessages.register();

        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, ModConfig.COMMON_SPEC);

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
    }
    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS){
            event.accept(ModItems.VOID_GENERATOR);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

            MenuScreens.register(ModMenuTypes.VOIDGENMENU.get(), VoidGenScreen::new);
        }
    }
}
