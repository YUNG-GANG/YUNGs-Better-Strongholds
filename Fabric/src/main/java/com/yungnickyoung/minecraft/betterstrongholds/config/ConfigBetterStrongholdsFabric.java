package com.yungnickyoung.minecraft.betterstrongholds.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class ConfigBetterStrongholdsFabric {
    @ConfigEntry.Category("General Settings")
    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip
    public ConfigGeneralFabric general = new ConfigGeneralFabric();

    @ConfigEntry.Category("Piece Settings")
    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip
    public ConfigPieceSettingsFabric pieceSettings = new ConfigPieceSettingsFabric();
}
