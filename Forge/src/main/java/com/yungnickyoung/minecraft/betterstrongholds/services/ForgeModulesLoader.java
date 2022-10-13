package com.yungnickyoung.minecraft.betterstrongholds.services;

import com.yungnickyoung.minecraft.betterstrongholds.module.ConfigModuleForge;

public class ForgeModulesLoader implements IModulesLoader {
    @Override
    public void loadModules() {
        IModulesLoader.super.loadModules();
        ConfigModuleForge.init();
    }
}
