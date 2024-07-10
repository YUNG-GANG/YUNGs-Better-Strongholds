package com.yungnickyoung.minecraft.betterstrongholds.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ConfigGeneralNeoForge {
    public final ModConfigSpec.ConfigValue<Boolean> enableStructureRuin;

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

        BUILDER.pop();
    }
}
