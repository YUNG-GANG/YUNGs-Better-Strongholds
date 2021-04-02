package com.yungnickyoung.minecraft.betterstrongholds.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigPieceSettings {
    public final ForgeConfigSpec.ConfigValue<Integer> grandLibraryMaxCount;
    public final ForgeConfigSpec.ConfigValue<Integer> smallLibraryMaxCount;
    public final ForgeConfigSpec.ConfigValue<Integer> prisonMaxCount;
    public final ForgeConfigSpec.ConfigValue<Integer> cmdAcariiMaxCount;
    public final ForgeConfigSpec.ConfigValue<Integer> cmdYungMaxCount;
    public final ForgeConfigSpec.ConfigValue<Integer> treasureRoomMaxCount;
    public final ForgeConfigSpec.ConfigValue<Integer> armouryLargeRoomMaxCount;
    public final ForgeConfigSpec.ConfigValue<Integer> armourySmallRoomMaxCount;
    public final ForgeConfigSpec.ConfigValue<Integer> portalRoomMaxCount;

    public ConfigPieceSettings(final ForgeConfigSpec.Builder BUILDER) {
        BUILDER
            .comment(
                "##########################################################################################################\n" +
                "# Options for controlling individual pieces/rooms in the stronghold.\n" +
                "##########################################################################################################")
            .push("Piece Settings");

        grandLibraryMaxCount = BUILDER
            .comment(
                " The max number of Grand Libraries that can spawn in a single stronghold.\n" +
                " Default: 1")
            .worldRestart()
            .define("Grand Library Max Count", 1);

        smallLibraryMaxCount = BUILDER
            .comment(
                " The max number of small libraries that can spawn in a single stronghold.\n" +
                " Note that these are distinct rooms from the Grand Library, being smaller\n" +
                " and more common (by default).\n" +
                " Default: 2")
            .worldRestart()
            .define("Small Library Max Count", 2);

        prisonMaxCount = BUILDER
            .comment(
                " The max number of prisons that can spawn in a single stronghold.\n" +
                " Default: 2")
            .worldRestart()
            .define("Prison Max Count", 2);

        cmdAcariiMaxCount = BUILDER
            .comment(
                " The max number of Commander (Acarii) rooms that can spawn in a single stronghold.\n" +
                " Default: 1")
            .worldRestart()
            .define("Commander Room (Acarii) Max Count", 1);

        cmdYungMaxCount = BUILDER
            .comment(
                " The max number of Commander (YUNG) rooms that can spawn in a single stronghold.\n" +
                " Default: 1")
            .worldRestart()
            .define("Commander Room (YUNG) Max Count", 1);

        treasureRoomMaxCount = BUILDER
            .comment(
                " The max number of treasure rooms that can spawn in a single stronghold.\n" +
                " Default: 2")
            .worldRestart()
            .define("Treasure Room Max Count", 2);

        armouryLargeRoomMaxCount = BUILDER
            .comment(
                " The max number of large armoury rooms that can spawn in a single stronghold.\n" +
                " Default: 2")
            .worldRestart()
            .define("Armoury Room (Large) Max Count", 2);

        armourySmallRoomMaxCount = BUILDER
            .comment(
                " The max number of small armoury rooms that can spawn in a single stronghold.\n" +
                " Default: 2")
            .worldRestart()
            .define("Armoury Room (Small) Max Count", 2);

        portalRoomMaxCount = BUILDER
            .comment(
                " The max number of portal rooms that can spawn in a single stronghold.\n" +
                " Default: 1")
            .worldRestart()
            .define("Portal Room Max Count", 1);

        BUILDER.pop();
    }
}
