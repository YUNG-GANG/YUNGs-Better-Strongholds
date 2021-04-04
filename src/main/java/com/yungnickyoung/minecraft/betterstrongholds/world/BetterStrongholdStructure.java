package com.yungnickyoung.minecraft.betterstrongholds.world;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholds;
import com.yungnickyoung.minecraft.betterstrongholds.config.BSConfig;
import com.yungnickyoung.minecraft.betterstrongholds.world.jigsaw.JigsawConfig;
import com.yungnickyoung.minecraft.betterstrongholds.world.jigsaw.JigsawManager;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BetterStrongholdStructure extends Structure<NoFeatureConfig> {
    public BetterStrongholdStructure(Codec<NoFeatureConfig> p_i231996_1_) {
        super(p_i231996_1_);
    }

    @Override
    public IStartFactory<NoFeatureConfig> getStartFactory() {
        return BetterStrongholdStructure.Start::new;
    }

    @Override
    public GenerationStage.Decoration getDecorationStage() {
        return GenerationStage.Decoration.STRONGHOLDS;
    }

    /**
     * Returns the name displayed when the locate command is used.
     * I believe (not 100% sure) that the lowercase form of this value must also match
     * the key of the entry added to Structure.field_236365_a_ during common setup.
     */
    @Override
    public String getStructureName() {
        return "Better Stronghold";
    }

    /**
     * shouldStartAt
     *
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
    @Override
    protected boolean func_230363_a_(ChunkGenerator chunkGenerator, BiomeProvider biomeProvider, long seed, SharedSeedRandom chunkRandom, int xChunk, int zChunk, Biome biome, ChunkPos chunkPos, NoFeatureConfig featureConfig) {
        int ringThickness = 96;
        int distanceToFirstRing = 80;

        int chunkDistance = (int) Math.sqrt((xChunk * xChunk) + (zChunk * zChunk));

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

    public static class Start extends StructureStart<NoFeatureConfig> {
        public Start(Structure<NoFeatureConfig> structureIn, int chunkX, int chunkZ, MutableBoundingBox mutableBoundingBox, int referenceIn, long seedIn) {
            super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
        }

        @Override
        public void func_230364_a_(DynamicRegistries dynamicRegistryManager, ChunkGenerator chunkGenerator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, NoFeatureConfig config) {
            // Turns the chunk coordinates into actual coordinates we can use. (Gets center of that chunk)
            int x = (chunkX << 4) + 7;
            int z = (chunkZ << 4) + 7;

            int minY = BSConfig.general.strongholdStartMinY.get();
            int maxY = BSConfig.general.strongholdStartMaxY.get();
            int y = rand.nextInt(maxY - minY) + minY;

            BlockPos blockpos = new BlockPos(x, y, z);
            JigsawConfig villageConfig = new JigsawConfig(
                () -> dynamicRegistryManager.getRegistry(Registry.JIGSAW_POOL_KEY)
                    .getOrDefault(new ResourceLocation(BetterStrongholds.MOD_ID, "starts")),
                BSConfig.general.strongholdSize.get()
            );

            // Generate the structure
            JigsawManager.assembleJigsawStructure(
                dynamicRegistryManager,
                villageConfig,
                chunkGenerator,
                templateManagerIn,
                blockpos,
                this.components,
                this.rand,
                false,
                false
            );

            // Sets the bounds of the structure once you are finished.
            this.recalculateStructureSize();

            // Debug log the coordinates of the center starting piece.
            BetterStrongholds.LOGGER.debug("Better Stronghold at {} {} {}",
                this.components.get(0).getBoundingBox().minX,
                this.components.get(0).getBoundingBox().minY,
                this.components.get(0).getBoundingBox().minZ
            );
        }
    }
}
