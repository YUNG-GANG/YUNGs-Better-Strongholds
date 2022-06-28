package com.yungnickyoung.minecraft.betterstrongholds.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class ConfigGeneralFabric {
    @ConfigEntry.Gui.Tooltip(count = 2)
    public double cobwebReplacementChanceNormal = .1;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public double cobwebReplacementChanceSpawner = .3;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public double torchSpawnRate = .1;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public double lanternSpawnRate = .2;
}
