package com.yungnickyoung.minecraft.betterstrongholds.services;

import com.yungnickyoung.minecraft.betterstrongholds.module.StructurePlacementTypeModule;
import com.yungnickyoung.minecraft.betterstrongholds.module.StructureProcessorTypeModule;

public interface IModulesLoader {
    default void loadModules() {
        StructureProcessorTypeModule.init();
        StructurePlacementTypeModule.init();
    }
}
