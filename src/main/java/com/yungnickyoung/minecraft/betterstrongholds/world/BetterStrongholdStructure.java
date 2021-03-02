package com.yungnickyoung.minecraft.betterstrongholds.world;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholds;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.jigsaw.SingleJigsawPiece;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.TemplateManager;
import org.apache.commons.lang3.mutable.MutableObject;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Random;

@MethodsReturnNonnullByDefault
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
     * || ONLY WORKS IN FORGE 34.1.12+ ||
     *
     * This method allows us to have mobs that spawn naturally over time in our structure.
     * No other mobs will spawn in the structure of the same entity classification.
     * The reason you want to match the classifications is so that your structure's mob
     * will contribute to that classification's cap. Otherwise, it may cause a runaway
     * spawning of the mob that will never stop.
     *
     * NOTE: getDefaultSpawnList is for monsters only and getDefaultCreatureSpawnList is
     *       for creatures only. If you want to add entities of another classification,
     *       use the StructureSpawnListGatherEvent to add water_creatures, water_ambient,
     *       ambient, or misc mobs. Use that event to add/remove mobs from structures
     *       that are not your own.
     *
     * NOTE 2: getSpawnList and getCreatureSpawnList is the vanilla methods that Forge does
     *         not hook up. Do not use those methods or else the mobs won't spawn. You would
     *         have to manually implement spawning for them. Stick with Forge's Default form
     *         as it is easier to use that.
     */
    private static final List<MobSpawnInfo.Spawners> STRUCTURE_MONSTERS = ImmutableList.of(
        new MobSpawnInfo.Spawners(EntityType.ILLUSIONER, 100, 4, 9),
        new MobSpawnInfo.Spawners(EntityType.VINDICATOR, 100, 4, 9)
    );
    @Override
    public List<MobSpawnInfo.Spawners> getDefaultSpawnList() {
        return STRUCTURE_MONSTERS;
    }

    private static final List<MobSpawnInfo.Spawners> STRUCTURE_CREATURES = ImmutableList.of(
        new MobSpawnInfo.Spawners(EntityType.SHEEP, 30, 10, 15),
        new MobSpawnInfo.Spawners(EntityType.RABBIT, 100, 1, 2)
    );
    @Override
    public List<MobSpawnInfo.Spawners> getDefaultCreatureSpawnList() {
        return STRUCTURE_CREATURES;
    }

    /**
     * shouldStartAt
     *
     * Vanilla has its own complex behavior for stronghold spawning.
     * We don't worry about that here - instead, we just prevent spawning close to the initial world spawn.
     * This is identical behavior to the stronghold in Repurposed Structures.
     */
    @Override
    protected boolean func_230363_a_(ChunkGenerator chunkGenerator, BiomeProvider biomeProvider, long seed, SharedSeedRandom chunkRandom, int chunkX, int chunkZ, Biome biome, ChunkPos chunkPos, NoFeatureConfig featureConfig) {
        return (chunkX * chunkX) + (chunkZ * chunkZ) > 10000;
    }

    public static class Start extends StructureStart<NoFeatureConfig> {
        public Start(Structure<NoFeatureConfig> structureIn, int chunkX, int chunkZ, MutableBoundingBox mutableBoundingBox, int referenceIn, long seedIn) {
            super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
        }

        @Override
        @ParametersAreNonnullByDefault
        public void func_230364_a_(DynamicRegistries dynamicRegistryManager, ChunkGenerator chunkGenerator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, NoFeatureConfig config) {
            // Turns the chunk coordinates into actual coordinates we can use. (Gets center of that chunk)
            int x = (chunkX << 4) + 7;
            int z = (chunkZ << 4) + 7;

            /*
             * We pass this into func_242837_a to tell it where to generate the structure.
             * If func_242837_a's last parameter is true, blockpos's Y value is ignored and the
             * structure will spawn at terrain height instead. Set that parameter to false to
             * force the structure to spawn at blockpos's Y value instead. You got options here!
             */
            BlockPos blockpos = new BlockPos(x, 40, z);

            // All a structure has to do is call this method to turn it into a jigsaw based structure!
            assembleJigsawStructure(
                dynamicRegistryManager,
                new VillageConfig(() -> dynamicRegistryManager.getRegistry(Registry.JIGSAW_POOL_KEY)
                    // The path to the starting Template Pool JSON file to read.
                    .getOrDefault(new ResourceLocation(BetterStrongholds.MOD_ID, "starts")),

                    // How many pieces outward from center can a recursive jigsaw structure spawn.
                    // Our structure is only 1 piece outward and isn't recursive so any value of 1 or more doesn't change anything.
                    // However, I recommend you keep this a decent value like 10 so people can use datapacks to add additional pieces to your structure easily.
                    // But don't make it too large for recursive structures like villages or you'll crash server due to hundreds of pieces attempting to generate!
                    19
                ),
                AbstractVillagePiece::new,
                chunkGenerator,
                templateManagerIn,
                blockpos, // Position of the structure. Y value is ignored if last parameter is set to true.
                this.components, // The list that will be populated with the jigsaw pieces after this method.
                this.rand,
                false, // Special boundary adjustments for villages. It's... hard to explain. Keep this false and make your pieces not be partially intersecting.
                       // Either not intersecting or fully contained will make children pieces spawn just fine. It's easier that way.
                false // Place at heightmap (top land). Set this to false for structure to be place at the passed in blockpos's Y value instead.
                      // Definitely keep this false when placing structures in the nether as otherwise, heightmap placing will put the structure on the Bedrock roof.
            );

            // **THE FOLLOWING TWO LINES ARE OPTIONAL**
            //
            // Right here, you can do interesting stuff with the pieces in this.components such as offset the
            // center piece by 50 blocks up for no reason, remove repeats of a piece or add a new piece so
            // only 1 of that piece exists, etc. But you do not have access to the piece's blocks as this list
            // holds just the piece's size and positions. Blocks will be placed later in JigsawManager.
            //
            // In this case, we do `piece.offset` to raise pieces up by 1 block so that the house is not right on
            // the surface of water or sunken into land a bit.
            //
            // Then we extend the bounding box down by 1 by doing `piece.getBoundingBox().minY` which will cause the
            // land formed around the structure to be lowered and not cover the doorstep. You can raise the bounding
            // box to force the structure to be buried as well. This bounding box stuff with land is only for structures
            // that you added to Structure.field_236384_t_ field handles adding land around the base of structures.
            //
            // By lifting the house up by 1 and lowering the bounding box, the land at bottom of house will now be
            // flush with the surrounding terrain without blocking off the doorstep.
//            this.components.forEach(piece -> piece.offset(0, 1, 0));
//            this.components.forEach(piece -> piece.getBoundingBox().minY -= 1);

            // Sets the bounds of the structure once you are finished.
            this.recalculateStructureSize();

            // Vanilla method of adjusting y-coordinate
            this.func_214628_a(chunkGenerator.getSeaLevel(), this.rand, 10);

            // I use to debug and quickly find out if the structure is spawning or not and where it is.
            // This is returning the coordinates of the center starting piece.
            BetterStrongholds.LOGGER.debug("Better Stronghold at {} {} {}",
                this.components.get(0).getBoundingBox().minX,
                this.components.get(0).getBoundingBox().minY,
                this.components.get(0).getBoundingBox().minZ
            );
        }

        /**
         * This method is a reimplementation of {@link JigsawManager#func_242837_a(DynamicRegistries, VillageConfig, JigsawManager.IPieceFactory, ChunkGenerator, TemplateManager, BlockPos, List, Random, boolean, boolean) JigsawManager.func_242837_a()}.
         *
         *
         */
        public static void assembleJigsawStructure(
            DynamicRegistries dynamicRegistryManager,
            VillageConfig jigsawConfig,
            JigsawManager.IPieceFactory pieceFactory,
            ChunkGenerator chunkGenerator,
            TemplateManager templateManager,
            BlockPos startPos,
            List<? super AbstractVillagePiece> components,
            Random random,
            boolean doBoundaryAdjustments,
            boolean useHeightmap
        ) {
            Structure.init();
            MutableRegistry<JigsawPattern> mutableregistry = dynamicRegistryManager.getRegistry(Registry.JIGSAW_POOL_KEY);
            Rotation rotation = Rotation.randomRotation(random);
            JigsawPattern jigsawpattern = jigsawConfig.func_242810_c().get();
            JigsawPiece jigsawpiece = jigsawpattern.getRandomPiece(random);
            AbstractVillagePiece abstractvillagepiece = pieceFactory.create(templateManager, jigsawpiece, startPos, jigsawpiece.getGroundLevelDelta(), rotation, jigsawpiece.getBoundingBox(templateManager, startPos, rotation));
            MutableBoundingBox mutableboundingbox = abstractvillagepiece.getBoundingBox();
            int i = (mutableboundingbox.maxX + mutableboundingbox.minX) / 2;
            int j = (mutableboundingbox.maxZ + mutableboundingbox.minZ) / 2;
            int k;
            if (useHeightmap) {
                k = startPos.getY() + chunkGenerator.getNoiseHeight(i, j, Heightmap.Type.WORLD_SURFACE_WG);
            } else {
                k = startPos.getY();
            }

            int l = mutableboundingbox.minY + abstractvillagepiece.getGroundLevelDelta();
            abstractvillagepiece.offset(0, k - l, 0);
            components.add(abstractvillagepiece);
            if (jigsawConfig.func_236534_a_() > 0) {
                AxisAlignedBB axisalignedbb = new AxisAlignedBB(i - 80, k - 80, j - 80, i + 80 + 1, k + 80 + 1, j + 80 + 1);
                JigsawManager.Assembler jigsawmanager$assembler = new JigsawManager.Assembler(mutableregistry, jigsawConfig.func_236534_a_(), pieceFactory, chunkGenerator, templateManager, components, random);
                jigsawmanager$assembler.availablePieces.addLast(new JigsawManager.Entry(abstractvillagepiece, new MutableObject<>(VoxelShapes.combineAndSimplify(VoxelShapes.create(axisalignedbb), VoxelShapes.create(AxisAlignedBB.toImmutable(mutableboundingbox)), IBooleanFunction.ONLY_FIRST)), k + 80, 0));

                while (!jigsawmanager$assembler.availablePieces.isEmpty()) {
                    JigsawManager.Entry jigsawmanager$entry = jigsawmanager$assembler.availablePieces.removeFirst();
                    SingleJigsawPiece piece = (SingleJigsawPiece) jigsawmanager$entry.villagePiece.getJigsawPiece();
                    String pieceName = piece.field_236839_c_.left().isPresent() ? piece.field_236839_c_.left().get().toString() : "INVALID!";
//                    BetterStrongholds.LOGGER.info(pieceName);
                    jigsawmanager$assembler.func_236831_a_(jigsawmanager$entry.villagePiece, jigsawmanager$entry.free, jigsawmanager$entry.boundsTop, jigsawmanager$entry.depth, doBoundaryAdjustments);
                }
            }
        }
    }
}
