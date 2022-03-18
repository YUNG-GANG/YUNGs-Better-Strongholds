package com.yungnickyoung.minecraft.betterstrongholds.world.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class BetterStrongholdFeatureConfiguration implements FeatureConfiguration {
    public static final Codec<BetterStrongholdFeatureConfiguration> CODEC = RecordCodecBuilder.create((codecBuilder) -> codecBuilder
            .group(
                    ResourceLocation.CODEC.fieldOf("start_pool").forGetter(config -> config.startPool),
                    Codec.INT.fieldOf("size").forGetter(config -> config.maxDepth))
            .apply(codecBuilder, BetterStrongholdFeatureConfiguration::new));

    public ResourceLocation startPool;
    public int maxDepth;

    public BetterStrongholdFeatureConfiguration(ResourceLocation startPool, int maxDepth) {
        this.startPool = startPool;
        this.maxDepth = maxDepth;
    }
}
