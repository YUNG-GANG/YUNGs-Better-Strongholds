package com.yungnickyoung.minecraft.betterstrongholds.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class BSConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<String> whitelistedDimensions;

    public static final ConfigGeneral general;
    public static final ConfigPieceSettings pieceSettings;

    static {
        BUILDER.push("YUNG's Better Strongholds");

        whitelistedDimensions = BUILDER
            .comment(
                " List of dimensions that will have Better Strongholds.\n" +
                " List must be comma-separated values enclosed in square brackets.\n" +
                " Entries must have the mod namespace included.\n" +
                " For example: \"[minecraft:overworld, minecraft:the_nether, undergarden:undergarden]\"\n" +
                " Default: \"[minecraft:overworld]\"")
            .worldRestart()
            .define("Whitelisted Dimensions", "[minecraft:overworld]");

        general = new ConfigGeneral(BUILDER);
        pieceSettings = new ConfigPieceSettings(BUILDER);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
