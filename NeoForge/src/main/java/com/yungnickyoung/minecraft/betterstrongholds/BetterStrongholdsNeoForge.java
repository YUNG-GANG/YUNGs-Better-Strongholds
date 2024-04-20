package com.yungnickyoung.minecraft.betterstrongholds;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(BetterStrongholdsCommon.MOD_ID)
public class BetterStrongholdsNeoForge {
    public static IEventBus loadingContextEventBus;

    public BetterStrongholdsNeoForge(IEventBus eventBus) {
        BetterStrongholdsNeoForge.loadingContextEventBus = eventBus;

        BetterStrongholdsCommon.init();
    }
}
