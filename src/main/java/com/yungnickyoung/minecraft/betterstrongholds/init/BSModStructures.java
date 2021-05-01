package com.yungnickyoung.minecraft.betterstrongholds.init;

import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholds;
import com.yungnickyoung.minecraft.betterstrongholds.world.BetterStrongholdStructure;
import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.StructureConfig;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class BSModStructures {
    public static StructureFeature<DefaultFeatureConfig> BETTER_STRONGHOLD = new BetterStrongholdStructure(DefaultFeatureConfig.CODEC);

    public static void init() {
        registerStructures();
    }

    private static void registerStructures() {
        FabricStructureBuilder
            .create(new Identifier(BetterStrongholds.MOD_ID, "stronghold"), BETTER_STRONGHOLD)
            .step(GenerationStep.Feature.STRONGHOLDS)
            .defaultConfig(new StructureConfig(85, 50, 596441294))
            .superflatFeature(BETTER_STRONGHOLD.configure(FeatureConfig.DEFAULT))
            .register();
    }
}