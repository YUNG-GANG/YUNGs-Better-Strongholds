package com.yungnickyoung.minecraft.betterstrongholds;

import com.google.common.collect.Lists;
import com.yungnickyoung.minecraft.betterstrongholds.config.BSConfig;
import com.yungnickyoung.minecraft.betterstrongholds.init.BSModConfig;
import com.yungnickyoung.minecraft.betterstrongholds.init.BSModConfiguredStructures;
import com.yungnickyoung.minecraft.betterstrongholds.init.BSModProcessors;
import com.yungnickyoung.minecraft.betterstrongholds.init.BSModStructures;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class BetterStrongholds implements ModInitializer {
    public static final String MOD_ID = "betterstrongholds";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    /** Better Strongholds config. Uses AutoConfig. **/
    public static BSConfig CONFIG;

    /**
     * Lists of whitelisted dimensions and blacklisted biomes.
     * Will be reinitialized later w/ values from config.
     */
    public static List<String> whitelistedDimensions = Lists.newArrayList("minecraft:overworld");
    public static List<String> blacklistedBiomes = Lists.newArrayList(
        "minecraft:ocean", "minecraft:frozen_ocean", "minecraft:deep_ocean",
        "minecraft:warm_ocean", "minecraft:lukewarm_ocean", "minecraft:cold_ocean",
        "minecraft:deep_lukewarm_ocean", "minecraft:deep_cold_ocean", "minecraft:deep_frozen_ocean",
        "minecraft:beach", "minecraft:snowy_beach",
        "minecraft:river", "minecraft:frozen_river"
    );

    @Override
    public void onInitialize() {
        BSModConfig.init();
        BSModProcessors.init();
        BSModStructures.init();
        BSModConfiguredStructures.init();
    }
}
