package com.yungnickyoung.minecraft.betterstrongholds.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name="betterstrongholds-fabric-1_21")
public class BSConfigFabric implements ConfigData {
    @ConfigEntry.Category("Better Strongholds")
    @ConfigEntry.Gui.TransitiveObject
    public ConfigBetterStrongholdsFabric betterStrongholds = new ConfigBetterStrongholdsFabric();
}