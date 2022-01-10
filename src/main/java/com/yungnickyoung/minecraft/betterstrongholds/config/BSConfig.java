package com.yungnickyoung.minecraft.betterstrongholds.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class BSConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<String> whitelistedDimensions;
    public static final ForgeConfigSpec.ConfigValue<String> blacklistedBiomes;

    public static final ConfigGeneral general;
    public static final ConfigPieceSettings pieceSettings;

    static {
        BUILDER.push("YUNG's Better Strongholds");

        whitelistedDimensions = BUILDER
                .comment(
                        """
                                List of dimensions that will have Better Strongholds.
                                List must be comma-separated values enclosed in square brackets.
                                Entries must have the mod namespace included.
                                For example: "[minecraft:overworld, minecraft:the_nether, undergarden:undergarden]"
                                Default: "[minecraft:overworld]\"""".indent(1))
                .worldRestart()
                .define("Whitelisted Dimensions", "[minecraft:overworld]");

        blacklistedBiomes = BUILDER
                .comment(
                        """
                                List of biomes that will NOT have Better Strongholds.
                                List must be comma-separated values enclosed in square brackets.
                                Entries must have the mod namespace included.
                                For example: "[minecraft:plains, byg:alps]"
                                Default: "[minecraft:ocean, minecraft:frozen_ocean, minecraft:deep_ocean, minecraft:warm_ocean, minecraft:lukewarm_ocean, minecraft:cold_ocean, minecraft:deep_lukewarm_ocean, minecraft:deep_cold_ocean, minecraft:deep_frozen_ocean, minecraft:beach, minecraft:snowy_beach, minecraft:river, minecraft:frozen_river]\"""".indent(1))
                .worldRestart()
                .define("Blacklisted Biomes", "[minecraft:ocean, minecraft:frozen_ocean, minecraft:deep_ocean, minecraft:warm_ocean, minecraft:lukewarm_ocean, minecraft:cold_ocean, minecraft:deep_lukewarm_ocean, minecraft:deep_cold_ocean, minecraft:deep_frozen_ocean, minecraft:beach, minecraft:snowy_beach, minecraft:river, minecraft:frozen_river]");

        general = new ConfigGeneral(BUILDER);
        pieceSettings = new ConfigPieceSettings(BUILDER);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}