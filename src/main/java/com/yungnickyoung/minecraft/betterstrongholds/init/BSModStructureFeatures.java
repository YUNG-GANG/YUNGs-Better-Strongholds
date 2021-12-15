package com.yungnickyoung.minecraft.betterstrongholds.init;

import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholds;
import com.yungnickyoung.minecraft.betterstrongholds.world.BetterStrongholdStructureFeature;
import com.yungnickyoung.minecraft.betterstrongholds.world.jigsaw.BetterStrongholdsFeatureConfiguration;
import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;

public class BSModStructureFeatures {
    public static StructureFeature<BetterStrongholdsFeatureConfiguration> BETTER_STRONGHOLD = new BetterStrongholdStructureFeature(BetterStrongholdsFeatureConfiguration.CODEC);

    public static StructureFeatureConfiguration BETTER_STRONGHOLD_STRUCTURE_CONFIG = new StructureFeatureConfiguration(85, 50, 596441294);

    public static void init() {
        registerStructures();
    }

    private static void registerStructures() {
        FabricStructureBuilder
            .create(new ResourceLocation(BetterStrongholds.MOD_ID, "stronghold"), BETTER_STRONGHOLD)
            .step(GenerationStep.Decoration.STRONGHOLDS)
            .defaultConfig(BETTER_STRONGHOLD_STRUCTURE_CONFIG)
            .register();
    }
}