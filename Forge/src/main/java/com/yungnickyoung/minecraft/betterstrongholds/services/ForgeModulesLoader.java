package com.yungnickyoung.minecraft.betterstrongholds.services;

import com.yungnickyoung.minecraft.betterstrongholds.module.*;

public class ForgeModulesLoader implements IModulesLoader {
    @Override
    public void loadModules() {
        ConfigModuleForge.init();
        TagModuleForge.init();
        StructureProcessorModuleForge.init();
        StructureFeatureModuleForge.init();
    }
}
