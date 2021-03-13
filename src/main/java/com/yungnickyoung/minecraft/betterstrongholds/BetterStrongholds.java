package com.yungnickyoung.minecraft.betterstrongholds;

import com.yungnickyoung.minecraft.betterstrongholds.init.ModProcessors;
import com.yungnickyoung.minecraft.betterstrongholds.init.ModStructures;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(BetterStrongholds.MOD_ID)
public class BetterStrongholds {
    public static final String MOD_ID = "betterstrongholds";
    public static final Logger LOGGER = LogManager.getLogger();

    public BetterStrongholds() {
        init();
    }

    private void init() {
        ModProcessors.init();
        ModStructures.init();
    }
}
