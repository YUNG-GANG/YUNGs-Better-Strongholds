package com.yungnickyoung.minecraft.betterstrongholds.init;

import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholds;
import com.yungnickyoung.minecraft.betterstrongholds.config.BSConfig;
import com.yungnickyoung.minecraft.betterstrongholds.world.jigsaw.BetterStrongholdsFeatureConfiguration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;

public class BSModConfiguredStructures {
    public static ConfiguredStructureFeature<?, ?> CONFIGURED_BETTER_STRONGHOLD = BSModStructureFeatures.BETTER_STRONGHOLD.get()
            .configured(new BetterStrongholdsFeatureConfiguration(new ResourceLocation(BetterStrongholds.MOD_ID, "starts"), BSConfig.general.strongholdSize.get()));
}
