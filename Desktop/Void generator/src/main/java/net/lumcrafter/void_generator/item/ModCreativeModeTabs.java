package net.lumcrafter.void_generator.item;

import net.lumcrafter.void_generator.Void_generator;
import net.lumcrafter.void_generator.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.awt.*;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Void_generator.MODID);

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }

    public static final RegistryObject<CreativeModeTab> VOID_GENERATOR_TAB = CREATIVE_MODE_TABS.register("void_generator_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.VOID_GENERATOR.get()))
                    .title(Component.translatable("creativetab.void_generator_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.VOID_GENERATOR.get());
                        pOutput.accept(ModBlocks.VOID_GENERATOR_BLOCK.get());
                    })
                    .build());


}
