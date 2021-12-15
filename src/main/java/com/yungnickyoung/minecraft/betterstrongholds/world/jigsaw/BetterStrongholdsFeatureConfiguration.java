package com.yungnickyoung.minecraft.betterstrongholds.world.jigsaw;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class BetterStrongholdsFeatureConfiguration implements FeatureConfiguration {
    public static final Codec<BetterStrongholdsFeatureConfiguration> CODEC = RecordCodecBuilder.create((codecBuilder) -> codecBuilder
            .group(
                    ResourceLocation.CODEC.fieldOf("start_pool").forGetter(BetterStrongholdsFeatureConfiguration::getStartPool),
                    Codec.INT.fieldOf("size").forGetter(BetterStrongholdsFeatureConfiguration::getMaxDepth))
            .apply(codecBuilder, BetterStrongholdsFeatureConfiguration::new));

    private final ResourceLocation startPool;

    /**
     * The size of the structure.
     * This is the max distance in Jigsaw pieces from the starting piece that pieces will be placed
     * before terminators are used.
     */
    private final int maxDepth;

    public BetterStrongholdsFeatureConfiguration(ResourceLocation startPool, int maxDepth) {
        this.startPool = startPool;
        this.maxDepth = maxDepth;
    }

    public int getMaxDepth() {
        return this.maxDepth;
    }

    public ResourceLocation getStartPool() {
        return this.startPool;
    }
}
