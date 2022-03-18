package com.yungnickyoung.minecraft.betterstrongholds;

import net.fabricmc.api.ModInitializer;

public class BetterStrongholdsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        BetterStrongholdsCommon.init();
    }
}
