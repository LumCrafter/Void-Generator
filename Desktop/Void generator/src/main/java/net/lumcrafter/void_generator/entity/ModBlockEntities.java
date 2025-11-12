package net.lumcrafter.void_generator.entity;

import net.lumcrafter.void_generator.Void_generator;
import net.lumcrafter.void_generator.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Void_generator.MODID);

    public static final RegistryObject<BlockEntityType<VoidGenBlock1Entity>> VOID_GEN_I =
            BLOCK_ENTITIES.register("void_gen_i", () ->
                    BlockEntityType.Builder.of(VoidGenBlock1Entity::new,
                            ModBlocks.VOID_GENERATOR_BLOCK.get()).build(null));
    public static void register(IEventBus eventBus){
        BLOCK_ENTITIES.register(eventBus);
    }
}
