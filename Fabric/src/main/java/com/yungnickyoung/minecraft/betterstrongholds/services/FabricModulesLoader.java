package com.yungnickyoung.minecraft.betterstrongholds.services;

import com.yungnickyoung.minecraft.betterstrongholds.module.*;

public class FabricModulesLoader implements IModulesLoader {
    @Override
    public void loadModules() {
        ConfigModuleFabric.init();
        TagModuleFabric.init();
        StructureProcessorModuleFabric.init();
        StructureFeatureModuleFabric.init();
    }
}
