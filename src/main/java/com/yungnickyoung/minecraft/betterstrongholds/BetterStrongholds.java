package com.yungnickyoung.minecraft.betterstrongholds;

import com.yungnickyoung.minecraft.betterstrongholds.init.BSModConfig;
import com.yungnickyoung.minecraft.betterstrongholds.init.BSModProcessors;
import com.yungnickyoung.minecraft.betterstrongholds.init.BSModStructureFeatures;
import com.yungnickyoung.minecraft.betterstrongholds.init.BSModTags;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(BetterStrongholds.MOD_ID)
public class BetterStrongholds {
    public static final String MOD_ID = "betterstrongholds";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public BetterStrongholds() {
        init();
    }

    private void init() {
        BSModConfig.init();
        BSModTags.init();
        BSModProcessors.init();
        BSModStructureFeatures.init();
    }
}
