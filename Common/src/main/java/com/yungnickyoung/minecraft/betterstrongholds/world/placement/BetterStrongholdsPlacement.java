package com.yungnickyoung.minecraft.betterstrongholds.world.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yungnickyoung.minecraft.betterstrongholds.module.StructurePlacementTypeModule;
import net.minecraft.core.Vec3i;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;

import java.util.Optional;

public class BetterStrongholdsPlacement extends RandomSpreadStructurePlacement {
    public static final Codec<BetterStrongholdsPlacement> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Vec3i.offsetCodec(16).optionalFieldOf("locate_offset", Vec3i.ZERO).forGetter(BetterStrongholdsPlacement::locateOffset),
            StructurePlacement.FrequencyReductionMethod.CODEC.optionalFieldOf("frequency_reduction_method", StructurePlacement.FrequencyReductionMethod.DEFAULT).forGetter(BetterStrongholdsPlacement::frequencyReductionMethod),
            Codec.floatRange(0.0F, 1.0F).optionalFieldOf("frequency", 1.0F).forGetter(BetterStrongholdsPlacement::frequency),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("salt").forGetter(BetterStrongholdsPlacement::salt),
            StructurePlacement.ExclusionZone.CODEC.optionalFieldOf("exclusion_zone").forGetter(BetterStrongholdsPlacement::exclusionZone),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("spacing").forGetter(BetterStrongholdsPlacement::spacing),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("separation").forGetter(BetterStrongholdsPlacement::separation),
            RandomSpreadType.CODEC.optionalFieldOf("spread_type", RandomSpreadType.LINEAR).forGetter(BetterStrongholdsPlacement::spreadType),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("chunk_distance_to_first_ring").forGetter(BetterStrongholdsPlacement::chunkDistanceToFirstRing),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("ring_chunk_thickness").forGetter(BetterStrongholdsPlacement::ringChunkThickness),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("max_ring_section").forGetter(BetterStrongholdsPlacement::maxRingSection)
    ).apply(instance, instance.stable(BetterStrongholdsPlacement::new)));

    private final int chunkDistanceToFirstRing;
    private final int ringChunkThickness;
    private final Optional<Integer> maxRingSection;

    public BetterStrongholdsPlacement(Vec3i locateOffset,
                                      FrequencyReductionMethod frequencyReductionMethod,
                                      Float frequency,
                                      Integer salt,
                                      Optional<ExclusionZone> exclusionZone,
                                      Integer spacing,
                                      Integer separation,
                                      RandomSpreadType randomSpreadType,
                                      Integer chunkDistanceToFirstRing,
                                      Integer ringChunkThickness,
                                      Optional<Integer> maxRingSection) {
        super(locateOffset, frequencyReductionMethod, frequency, salt, exclusionZone, spacing, separation, randomSpreadType);
        this.chunkDistanceToFirstRing = chunkDistanceToFirstRing;
        this.ringChunkThickness = ringChunkThickness;
        this.maxRingSection = maxRingSection;
    }

    @Override
    protected boolean isPlacementChunk(ChunkGenerator chunkGenerator, RandomState randomState, long seed, int chunkX, int chunkZ) {
        ChunkPos chunkPos = this.getPotentialStructureChunk(seed, chunkX, chunkZ);
        if (chunkPos.x == chunkX && chunkPos.z == chunkZ) {
            int chunkDistance = (int) Math.sqrt((chunkX * chunkX) + (chunkZ * chunkZ));

            // Offset the distance so that the first ring is closer to spawn
            int shiftedChunkDistance = chunkDistance + (ringChunkThickness - chunkDistanceToFirstRing);

            // Determine which ring we are in.
            // Non-stronghold rings are even number ringSection.
            // Stronghold rings are odd number ringSection.
            int ringSection = shiftedChunkDistance / ringChunkThickness;

            // Limit number of rings, if max setting is present.
            if (maxRingSection.isPresent()) {
                if (ringSection > maxRingSection.get()) {
                    return false;
                }
            }

            // Only spawn strongholds on odd number sections
            return ringSection % 2 == 1;
        }
        return false;
    }

    @Override
    public StructurePlacementType<?> type() {
        return StructurePlacementTypeModule.BETTER_STRONGHOLD_PLACEMENT;
    }

    public int chunkDistanceToFirstRing() {
        return chunkDistanceToFirstRing;
    }

    public int ringChunkThickness() {
        return ringChunkThickness;
    }

    public Optional<Integer> maxRingSection() {
        return maxRingSection;
    }
}
