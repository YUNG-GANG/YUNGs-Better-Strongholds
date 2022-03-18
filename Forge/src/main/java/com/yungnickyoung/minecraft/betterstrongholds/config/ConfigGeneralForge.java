package com.yungnickyoung.minecraft.betterstrongholds.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigGeneralForge {
    public final ForgeConfigSpec.ConfigValue<Integer> strongholdStartMinY;
    public final ForgeConfigSpec.ConfigValue<Integer> strongholdStartMaxY;
    public final ForgeConfigSpec.ConfigValue<Integer> strongholdMaxY;
    public final ForgeConfigSpec.ConfigValue<Double> cobwebReplacementChanceNormal;
    public final ForgeConfigSpec.ConfigValue<Double> cobwebReplacementChanceSpawner;
    public final ForgeConfigSpec.ConfigValue<Double> torchSpawnRate;
    public final ForgeConfigSpec.ConfigValue<Double> lanternSpawnRate;

    public ConfigGeneralForge(final ForgeConfigSpec.Builder BUILDER) {
        BUILDER
                .comment(
                        """
                                ##########################################################################################################
                                # General settings.
                                ##########################################################################################################""")
                .push("General");

        strongholdStartMinY = BUILDER
                .comment(
                        """
                                The minimum y-value at which the STARTING ROOM of the stronghold can spawn.
                                Note that the various pieces of the stronghold can extend above or below this value.
                                Default: -30""".indent(1))
                .worldRestart()
                .define("Min Start Y", -30);

        strongholdStartMaxY = BUILDER
                .comment(
                        """
                                The maximum y-value at which the STARTING ROOM of the stronghold can spawn.
                                Note that the various pieces of the stronghold can extend above or below this value.
                                Default: 11""".indent(1))
                .worldRestart()
                .define("Max Start Y", 11);

        strongholdMaxY = BUILDER
                .comment(
                        """
                                The maximum y-value at which ANY piece of the stronghold can spawn.
                                If any piece attempts to spawn such that any part of it is above this y-value,
                                it will not spawn.
                                In other words, this is a hard cap above which no part of the stronghold can generate.
                                Default: 60""".indent(1))
                .worldRestart()
                .define("Max Y", 60);

        cobwebReplacementChanceNormal = BUILDER
                .comment(
                        " The rate at which cobwebs will spawn in various parts of the stronghold.\n" +
                        " Default: 0.1")
                .worldRestart()
                .define("Cobweb Spawn Rate (NORMAL)", 0.1);

        cobwebReplacementChanceSpawner = BUILDER
                .comment(
                        " The rate at which cobwebs will spawn around spider spawners in libraries.\n" +
                        " Default: 0.3")
                .worldRestart()
                .define("Cobweb Spawn Rate (SPAWNER)", 0.3);

        torchSpawnRate = BUILDER
                .comment(
                        " The rate at which torches spawn throughout the stronghold.\n" +
                        " Default: 0.1")
                .worldRestart()
                .define("Torch Spawn Rate", 0.1);

        lanternSpawnRate = BUILDER
                .comment(
                        " The rate at which lanterns spawn throughout the stronghold.\n" +
                        " Default: 0.2")
                .worldRestart()
                .define("Lantern Spawn Rate", 0.2);

        BUILDER.pop();
    }
}
