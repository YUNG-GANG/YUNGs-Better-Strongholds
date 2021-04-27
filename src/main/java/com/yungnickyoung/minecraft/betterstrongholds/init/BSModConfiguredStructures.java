package com.yungnickyoung.minecraft.betterstrongholds.init;

import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;

public class BSModConfiguredStructures {
    public static StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> CONFIGURED_BETTER_STRONGHOLD = BSModStructures.BETTER_STRONGHOLD.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);
}
