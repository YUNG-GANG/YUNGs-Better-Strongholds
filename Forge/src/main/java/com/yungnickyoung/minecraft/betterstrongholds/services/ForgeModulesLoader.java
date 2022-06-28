package com.yungnickyoung.minecraft.betterstrongholds.services;

import com.yungnickyoung.minecraft.betterstrongholds.module.ConfigModuleForge;
import com.yungnickyoung.minecraft.betterstrongholds.module.StructurePlacementTypeModule;
import com.yungnickyoung.minecraft.betterstrongholds.module.StructurePlacementTypeModuleForge;
import com.yungnickyoung.minecraft.betterstrongholds.module.StructureProcessorTypeModuleForge;

public class ForgeModulesLoader implements IModulesLoader {
    @Override
    public void loadModules() {
        IModulesLoader.super.loadModules();
        ConfigModuleForge.init();
        StructureProcessorTypeModuleForge.init();
        StructurePlacementTypeModuleForge.init();
    }
}
