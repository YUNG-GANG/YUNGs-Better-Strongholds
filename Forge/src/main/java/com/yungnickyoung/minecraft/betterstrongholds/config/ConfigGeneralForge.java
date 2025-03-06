package com.yungnickyoung.minecraft.betterstrongholds.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigGeneralForge {
    public final ForgeConfigSpec.ConfigValue<Boolean> enableStructureRuin;
    public final ForgeConfigSpec.ConfigValue<Double> filledPortalFrameChance;

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

        filledPortalFrameChance = BUILDER
                .comment(
                        " The chance for each End Portal Frame block to spawn already filled with an Eye of Ender.\n" +
                        " Default: 0.1")
                .worldRestart()
                .define("Filled Portal Frame Chance", 0.1);

        BUILDER.pop();
    }
}
