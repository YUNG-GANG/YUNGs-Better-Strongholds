package com.yungnickyoung.minecraft.betterstrongholds.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigGeneralForge {
    public final ForgeConfigSpec.ConfigValue<Boolean> enableStructureRuin;

    public ConfigGeneralForge(final ForgeConfigSpec.Builder BUILDER) {
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
