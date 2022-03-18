package com.yungnickyoung.minecraft.betterstrongholds.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class ConfigPieceSettingsFabric {
    @ConfigEntry.Gui.Tooltip(count = 2)
    public int grandLibraryMaxCount = 1;

    @ConfigEntry.Gui.Tooltip(count = 4)
    public int smallLibraryMaxCount = 2;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public int prisonMaxCount = 2;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public int cmdAcariiMaxCount = 1;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public int cmdYungMaxCount = 1;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public int treasureRoomMaxCount = 2;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public int armouryLargeRoomMaxCount = 2;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public int armourySmallRoomMaxCount = 2;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public int portalRoomMaxCount = 1;
}
