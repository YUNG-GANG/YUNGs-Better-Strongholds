package com.yungnickyoung.minecraft.betterstrongholds;

import com.google.common.collect.Lists;
import com.yungnickyoung.minecraft.betterstrongholds.config.BSConfig;
import com.yungnickyoung.minecraft.betterstrongholds.init.BSModConfig;
import com.yungnickyoung.minecraft.betterstrongholds.init.BSModProcessors;
import com.yungnickyoung.minecraft.betterstrongholds.init.BSModStructures;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Mod(BetterStrongholds.MOD_ID)
public class BetterStrongholds {
    public static final String MOD_ID = "betterstrongholds";
    public static final Logger LOGGER = LogManager.getLogger();

    /**
     * List of whitelisted dimensions. Will be reinitialized later w/ values from config.
     */
    public static List<String> whitelistedDimensions = Lists.newArrayList("minecraft:overworld");

    public BetterStrongholds() {
        init();
    }

    private void init() {
        BSModProcessors.init();
        BSModStructures.init();
        BSModConfig.init();
    }
}
