package net.lumcrafter.void_generator.item;

import net.lumcrafter.void_generator.Void_generator;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Void_generator.MODID);

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
