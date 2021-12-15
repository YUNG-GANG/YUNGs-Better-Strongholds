package com.yungnickyoung.minecraft.betterstrongholds.config;

import com.google.common.collect.Lists;
import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholds;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name=BSSettings.CUSTOM_CONFIG_PATH + "-" + BSSettings.VERSION_PATH)
public class BSConfig implements ConfigData {
    @ConfigEntry.Category("Better Strongholds")
    @ConfigEntry.Gui.TransitiveObject
    public ConfigBetterStrongholds betterStrongholds = new ConfigBetterStrongholds();

    /**
     * Validate whitelisted dimensions on config load.
     */
    @Override
    public void validatePostLoad() throws ValidationException {
        // Dimension whitelisting
        String rawStringofList = betterStrongholds.whitelistedDimensions;
        int strLen = rawStringofList.length();

        // Validate the string's format
        if (strLen < 2 || rawStringofList.charAt(0) != '[' || rawStringofList.charAt(strLen - 1) != ']') {
            BetterStrongholds.LOGGER.error("INVALID VALUE FOR SETTING 'Whitelisted Dimensions'. Using [minecraft:overworld] instead...");
            BetterStrongholds.whitelistedDimensions = Lists.newArrayList("minecraft:overworld");
            return;
        }

        // Parse string to list
        BetterStrongholds.whitelistedDimensions = Lists.newArrayList(rawStringofList.substring(1, strLen - 1).split(",\\s*"));

        // Biome blacklisting
        rawStringofList = betterStrongholds.blacklistedBiomes;
        strLen = rawStringofList.length();

        // Validate the string's format
        if (strLen < 2 || rawStringofList.charAt(0) != '[' || rawStringofList.charAt(strLen - 1) != ']') {
            BetterStrongholds.LOGGER.error("INVALID VALUE FOR SETTING 'Blacklisted Biomes'. Using default instead...");
            BetterStrongholds.blacklistedBiomes = Lists.newArrayList(
                "minecraft:ocean", "minecraft:frozen_ocean", "minecraft:deep_ocean",
                "minecraft:warm_ocean", "minecraft:lukewarm_ocean", "minecraft:cold_ocean",
                "minecraft:deep_lukewarm_ocean", "minecraft:deep_cold_ocean", "minecraft:deep_frozen_ocean",
                "minecraft:beach", "minecraft:snowy_beach",
                "minecraft:river", "minecraft:frozen_river"
            );
            return;
        }

        // Parse string to list
        BetterStrongholds.blacklistedBiomes = Lists.newArrayList(rawStringofList.substring(1, strLen - 1).split(",\\s*"));
    }
}