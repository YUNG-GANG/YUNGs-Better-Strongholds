package com.yungnickyoung.minecraft.betterstrongholds.world.jigsaw;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.world.gen.feature.FeatureConfig;

import java.util.function.Supplier;

public class JigsawConfig implements FeatureConfig {
    public static final Codec<JigsawConfig> CODEC = RecordCodecBuilder.create((codecBuilder) -> codecBuilder
        .group(
            StructurePool.REGISTRY_CODEC.fieldOf("start_pool").forGetter(JigsawConfig::getStartPoolSupplier),
            Codec.intRange(0, 7).fieldOf("size").forGetter(JigsawConfig::getMaxChainPieceLength))
        .apply(codecBuilder, JigsawConfig::new));

    private final Supplier<StructurePool> startPoolSupplier;
    private final int size;

    public JigsawConfig(Supplier<StructurePool> startPoolSupplier, int size) {
        this.startPoolSupplier = startPoolSupplier;
        this.size = size;
    }

    public int getMaxChainPieceLength() {
        return this.size;
    }

    public Supplier<StructurePool> getStartPoolSupplier() {
        return this.startPoolSupplier;
    }
}
