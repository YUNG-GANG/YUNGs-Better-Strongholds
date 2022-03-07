package com.yungnickyoung.minecraft.betterstrongholds.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class BSConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ConfigGeneral general;
    public static final ConfigPieceSettings pieceSettings;

    static {
        BUILDER.push("YUNG's Better Strongholds");

        general = new ConfigGeneral(BUILDER);
        pieceSettings = new ConfigPieceSettings(BUILDER);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}