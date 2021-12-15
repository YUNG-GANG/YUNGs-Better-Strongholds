package com.yungnickyoung.minecraft.betterstrongholds.world;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholds;
import com.yungnickyoung.minecraft.betterstrongholds.world.jigsaw.BetterStrongholdsFeatureConfiguration;
import com.yungnickyoung.minecraft.betterstrongholds.world.jigsaw.JigsawManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.NoiseAffectingStructureFeature;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;

import java.util.Optional;

public class BetterStrongholdStructureFeature extends NoiseAffectingStructureFeature<BetterStrongholdsFeatureConfiguration> {
    public BetterStrongholdStructureFeature(Codec<BetterStrongholdsFeatureConfiguration> codec) {
        super(codec, context -> {
            // Only generate if location is valid
            if (!checkLocation(context)) {
                return Optional.empty();
            }

            // Determine starting BlockPos
            int x = context.chunkPos().getMinBlockX() + 7;
            int z = context.chunkPos().getMinBlockZ() + 7;
            int minY = BetterStrongholds.CONFIG.betterStrongholds.general.strongholdStartMinY;
            int maxY = BetterStrongholds.CONFIG.betterStrongholds.general.strongholdStartMaxY;
            WorldgenRandom worldgenRandom = new WorldgenRandom(new LegacyRandomSource(0L));
            worldgenRandom.setLargeFeatureSeed(context.seed(), context.chunkPos().x, context.chunkPos().z);
            int y = worldgenRandom.nextInt(maxY - minY) + minY;
            BlockPos blockPos = new BlockPos(x, y, z);

            // Generate
            return JigsawManager.assembleJigsawStructure(
                    context,
                    PoolElementStructurePiece::new,
                    blockPos,
                    false,
                    false,
                    80);
        });
    }

    /**
     * A less constrained form of the ring-based placement of vanilla strongholds.
     *
     * Thickness of rings: 1,536  (96 chunks)
     * Distance between rings: 1,536 (96 chunks)
     * Distance to first ring: 1,280 (80 chunks)
     *
     * Vanilla has 8 rings.
     *
     * Credits to TelepathicGrunt for this.
     */
    private static boolean checkLocation(PieceGeneratorSupplier.Context<BetterStrongholdsFeatureConfiguration> context) {
        ChunkPos chunkPos = context.chunkPos();

        int ringThickness = 96;
        int distanceToFirstRing = 80;

        int chunkDistance = (int) Math.sqrt((chunkPos.x * chunkPos.x) + (chunkPos.z * chunkPos.z));

        // Offset the distance so that the first ring is closer to spawn
        int shiftedChunkDistance = chunkDistance + (ringThickness - distanceToFirstRing);

        // Determine which ring we are in.
        // non-stronghold rings are even number ringSection
        // stronghold rings are odd number ringSection.
        int ringSection = shiftedChunkDistance / ringThickness;

        // Would mimic vanilla's 8 ring result
        // if(ringSection > 16) return false;

        // Only spawn strongholds on odd number sections
        return ringSection % 2 == 1;
    }
}
