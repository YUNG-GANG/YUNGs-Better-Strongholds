package com.yungnickyoung.minecraft.betterstrongholds.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigGeneral {
    public final ForgeConfigSpec.ConfigValue<Integer> strongholdStartMinY;
    public final ForgeConfigSpec.ConfigValue<Integer> strongholdStartMaxY;
    public final ForgeConfigSpec.ConfigValue<Integer> strongholdMaxY;
    public final ForgeConfigSpec.ConfigValue<Integer> strongholdSize;
    public final ForgeConfigSpec.ConfigValue<Float> cobwebReplacementChanceNormal;
    public final ForgeConfigSpec.ConfigValue<Float> cobwebReplacementChanceSpawner;
    public final ForgeConfigSpec.ConfigValue<Float> torchSpawnRate;
    public final ForgeConfigSpec.ConfigValue<Float> lanternSpawnRate;

    public ConfigGeneral(final ForgeConfigSpec.Builder BUILDER) {
        BUILDER
            .comment(
                "##########################################################################################################\n" +
                "# General settings.\n" +
                "##########################################################################################################")
            .push("General");

        strongholdStartMinY = BUILDER
            .comment(
                " The minimum y-value at which the STARTING ROOM of the stronghold can spawn.\n" +
                " Note that the various pieces of the stronghold can extend above or below this value.\n" +
                " Default: 30")
            .worldRestart()
            .define("Min Start Y", 30);

        strongholdStartMaxY = BUILDER
            .comment(
                " The maximum y-value at which the STARTING ROOM of the stronghold can spawn.\n" +
                " Note that the various pieces of the stronghold can extend above or below this value.\n" +
                " Default: 31")
            .worldRestart()
            .define("Max Start Y", 31);

        strongholdMaxY = BUILDER
            .comment(
                " The maximum y-value at which ANY piece of the stronghold can spawn.\n" +
                " If any piece attempts to spawn such that any part of it is above this y-value,\n" +
                " it will not spawn.\n" +
                " In other words, this is a hard cap above which no part of the stronghold can generate.\n" +
                " Default: 60")
            .worldRestart()
            .define("Max Y", 60);

        strongholdSize = BUILDER
            .comment(
                " The max number of \"pieces\" the stronghold will generate from the center.\n" +
                " This number controls the general size of strongholds. Bigger number = bigger strongholds.\n" +
                " It is HIGHLY recommended to keep this an even number!\n" +
                " Default: 16")
            .worldRestart()
            .define("Stronghold Size", 16);

        cobwebReplacementChanceNormal = BUILDER
            .comment(
                " The rate at which cobwebs will spawn in various parts of the stronghold.\n" +
                " Default: 0.1")
            .worldRestart()
            .define("Cobweb Spawn Rate (NORMAL)", 0.1f);

        cobwebReplacementChanceSpawner = BUILDER
            .comment(
                " The rate at which cobwebs will spawn around spider spawners in libraries.\n" +
                " Default: 0.3")
            .worldRestart()
            .define("Cobweb Spawn Rate (SPAWNER)", 0.3f);

        torchSpawnRate = BUILDER
            .comment(
                " The rate at which torches spawn throughout the stronghold.\n" +
                " Default: 0.1")
            .worldRestart()
            .define("Torch Spawn Rate", 0.1f);

        lanternSpawnRate = BUILDER
            .comment(
                " The rate at which lanterns spawn throughout the stronghold.\n" +
                " Default: 0.2")
            .worldRestart()
            .define("Lantern Spawn Rate", 0.2f);

        BUILDER.pop();
    }
}
