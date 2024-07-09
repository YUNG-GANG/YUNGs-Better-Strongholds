package com.yungnickyoung.minecraft.betterstrongholds;

import com.yungnickyoung.minecraft.betterstrongholds.module.ConfigModuleNeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(BetterStrongholdsCommon.MOD_ID)
public class BetterStrongholdsNeoForge {
    public static IEventBus loadingContextEventBus;

    public BetterStrongholdsNeoForge(IEventBus eventBus, ModContainer container) {
        BetterStrongholdsNeoForge.loadingContextEventBus = eventBus;

        BetterStrongholdsCommon.init();
        ConfigModuleNeoForge.init(container);
    }
}
