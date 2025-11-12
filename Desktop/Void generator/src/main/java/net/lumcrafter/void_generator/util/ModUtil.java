package net.lumcrafter.void_generator.util;
import net.lumcrafter.void_generator.config.ModConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import java.util.List;

public class ModUtil {

    public static Item getRandomConfiguredItem() {
        List<? extends String> itemStrings = ModConfig.COMMON.VOIDGEN_ITEMS.get();
        List<? extends Double> probs = ModConfig.COMMON.VOIDGEN_PROBABILITIES.get();

        double roll = Math.random();
        double cumulative = 0.0;

        for (int i = 0; i < itemStrings.size(); i++) {
            cumulative += probs.get(i);
            if (roll <= cumulative) {
                return BuiltInRegistries.ITEM.get(new ResourceLocation(itemStrings.get(i)));
            }
        }

        return BuiltInRegistries.ITEM.get(new ResourceLocation(itemStrings.get(itemStrings.size() - 1)));
    }
}
