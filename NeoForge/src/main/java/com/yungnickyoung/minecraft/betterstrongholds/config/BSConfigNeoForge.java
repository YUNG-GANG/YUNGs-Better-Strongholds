package com.yungnickyoung.minecraft.betterstrongholds.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class BSConfigNeoForge {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ConfigGeneralNeoForge general;

    static {
        BUILDER.push("YUNG's Better Strongholds");

        general = new ConfigGeneralNeoForge(BUILDER);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}