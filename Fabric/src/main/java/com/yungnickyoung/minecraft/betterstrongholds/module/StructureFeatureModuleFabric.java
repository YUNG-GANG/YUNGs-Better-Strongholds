package com.yungnickyoung.minecraft.betterstrongholds.module;

import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholdsCommon;
import com.yungnickyoung.minecraft.betterstrongholds.world.BetterStrongholdStructureFeature;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class StructureFeatureModuleFabric {
    public static void init() {
        StructureFeatureModule.BETTER_STRONGHOLD = register("stronghold", new BetterStrongholdStructureFeature());
    }

    private static <FC extends FeatureConfiguration> StructureFeature<FC> register(String name, StructureFeature<FC> structureFeature) {
        return Registry.register(Registry.STRUCTURE_FEATURE, new ResourceLocation(BetterStrongholdsCommon.MOD_ID, name), structureFeature);
    }
}
