package com.yungnickyoung.minecraft.betterstrongholds.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ConfigGeneralNeoForge {
    public final ModConfigSpec.ConfigValue<Boolean> enableStructureRuin;
    public final ModConfigSpec.ConfigValue<Double> filledPortalFrameChance;

    public ConfigGeneralNeoForge(final ModConfigSpec.Builder BUILDER) {
        BUILDER
                .comment(
                        """
                                ##########################################################################################################
                                # General settings.
                                ##########################################################################################################""")
                .push("General");

        enableStructureRuin = BUILDER
                .comment(
                        " Allows strongholds to be slightly destroyed by small noodle caves.\n" +
                                " Note that they will remain unaffected by large caverns.\n" +
                                " Default: false")
                .worldRestart()
                .define("Enable Structure Ruin", false);

        filledPortalFrameChance = BUILDER
                .comment(
                        " The chance for each End Portal Frame block to spawn already filled with an Eye of Ender.\n" +
                        " Default: 0.1")
                .worldRestart()
                .define("Filled Portal Frame Chance", 0.1);

        BUILDER.pop();
    }
}
