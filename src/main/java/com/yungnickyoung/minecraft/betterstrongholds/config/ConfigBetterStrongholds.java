package com.yungnickyoung.minecraft.betterstrongholds.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class ConfigBetterStrongholds {
    @ConfigEntry.Category("General Settings")
    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip
    public ConfigGeneral general = new ConfigGeneral();

    @ConfigEntry.Category("Piece Settings")
    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip
    public ConfigPieceSettings pieceSettings = new ConfigPieceSettings();

    @ConfigEntry.Gui.Tooltip(count = 5)
    public String whitelistedDimensions = "[minecraft:overworld]";

    @ConfigEntry.Gui.Tooltip(count = 5)
    public String blacklistedBiomes = "[minecraft:ocean, minecraft:frozen_ocean, minecraft:deep_ocean, minecraft:warm_ocean, minecraft:lukewarm_ocean, minecraft:cold_ocean, minecraft:deep_lukewarm_ocean, minecraft:deep_cold_ocean, minecraft:deep_frozen_ocean, minecraft:beach, minecraft:snowy_beach, minecraft:river, minecraft:frozen_river]";
}
