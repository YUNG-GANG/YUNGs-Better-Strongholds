package com.yungnickyoung.minecraft.betterstrongholds.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class ConfigGeneral {
    @ConfigEntry.Gui.Tooltip(count = 3)
    public int strongholdStartMinY = 30;

    @ConfigEntry.Gui.Tooltip(count = 3)
    public int strongholdStartMaxY = 31;

    @ConfigEntry.Gui.Tooltip(count = 5)
    public int strongholdMaxY = 60;

    @ConfigEntry.Gui.Tooltip(count = 4)
    public int strongholdSize = 16;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public double cobwebReplacementChanceNormal = .1;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public double cobwebReplacementChanceSpawner = .3;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public double torchSpawnRate = .1;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public double lanternSpawnRate = .2;
}
