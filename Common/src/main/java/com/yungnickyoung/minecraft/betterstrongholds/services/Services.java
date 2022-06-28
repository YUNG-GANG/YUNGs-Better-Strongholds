package com.yungnickyoung.minecraft.betterstrongholds.services;

import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholdsCommon;

import java.util.ServiceLoader;

public class Services {
    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);
    public static final IModulesLoader MODULES = load(IModulesLoader.class);
    public static final IRegistryHelper REGISTRY = load(IRegistryHelper.class);
    public static final IProcessorProvider PROCESSORS = load(IProcessorProvider.class);

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        BetterStrongholdsCommon.LOGGER.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}