package com.yungnickyoung.minecraft.betterstrongholds.services;

import com.yungnickyoung.minecraft.betterstrongholds.module.ConfigModuleNeoForge;

public class NeoForgeModulesLoader implements IModulesLoader {
    @Override
    public void loadModules() {
        IModulesLoader.super.loadModules();
        ConfigModuleNeoForge.init();
    }
}
