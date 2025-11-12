package net.lumcrafter.void_generator.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class ModConfig {

    public static final ForgeConfigSpec COMMON_SPEC;
    public static final CommonConfig COMMON;

    static {
        Pair<CommonConfig, ForgeConfigSpec> pair =
                new ForgeConfigSpec.Builder().configure(CommonConfig::new);
        COMMON = pair.getLeft();
        COMMON_SPEC = pair.getRight();
    }

    public static class CommonConfig {
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> VOIDGEN_ITEMS;
        public final ForgeConfigSpec.ConfigValue<List<? extends Double>> VOIDGEN_PROBABILITIES;

        public CommonConfig(ForgeConfigSpec.Builder builder) {
            builder.push("Void Generator Config");

            VOIDGEN_ITEMS = builder
                    .comment("List of possible items that the Void Generator can produce.")
                    .defineListAllowEmpty(
                            List.of("items"),
                            () -> List.of("minecraft:diamond", "minecraft:iron_ingot", "minecraft:coal", "minecraft:netherite_scrap"),
                            obj -> obj instanceof String
                    );

            VOIDGEN_PROBABILITIES = builder
                    .comment("All probabilities must have a sum of 1.0.")
                    .defineListAllowEmpty(
                            List.of("probabilities"),
                            () -> List.of(0.005, 0.3, 0.494, 0.001, 0.2),
                            obj -> obj instanceof Double
                    );

            builder.pop();
        }
    }
}
