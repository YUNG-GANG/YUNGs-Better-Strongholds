package com.yungnickyoung.minecraft.betterstrongholds.world.jigsaw;

import com.google.common.collect.Queues;
import com.mojang.datafixers.util.Pair;
import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholds;
import net.minecraft.block.JigsawBlock;
import net.minecraft.structure.*;
import net.minecraft.structure.pool.*;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.*;

/**
 * Reimplementation of {@link net.minecraft.structure.pool.StructurePoolBasedGenerator} with additional options.
 */
public class JigsawManager {
    public static void assembleJigsawStructure(
        DynamicRegistryManager dynamicRegistryManager,
        JigsawConfig jigsawConfig,
        ChunkGenerator chunkGenerator,
        StructureManager templateManager,
        BlockPos startPos, List<? super StructurePiece> components,
        Random random,
        boolean doBoundaryAdjustments,
        boolean useHeightmap
    ) {
        // Get jigsaw pool registry
        MutableRegistry<StructurePool> jigsawPoolRegistry = dynamicRegistryManager.get(Registry.TEMPLATE_POOL_WORLDGEN);

        // Get a random orientation for the starting piece
        BlockRotation rotation = BlockRotation.random(random);

        // Get starting pool
        StructurePool startPool = jigsawConfig.getStartPoolSupplier().get();

        // Grab a random starting piece from the start pool. This is just the piece design itself, without rotation or position information.
        // Think of it as a blueprint.
        StructurePoolElement startPieceBlueprint = startPool.getRandomElement(random);

        // Instantiate a piece using the "blueprint" we just got.
        PoolStructurePiece startPiece = new PoolStructurePiece(
            templateManager,
            startPieceBlueprint,
            startPos,
            startPieceBlueprint.getGroundLevelDelta(),
            rotation,
            startPieceBlueprint.getBoundingBox(templateManager, startPos, rotation)
        );

        // Store center position of starting piece's bounding box
        BlockBox pieceBoundingBox = startPiece.getBoundingBox();
        int pieceCenterX = (pieceBoundingBox.maxX + pieceBoundingBox.minX) / 2;
        int pieceCenterZ = (pieceBoundingBox.maxZ + pieceBoundingBox.minZ) / 2;
        int pieceCenterY = useHeightmap
            ? startPos.getY() + chunkGenerator.getHeightOnGround(pieceCenterX, pieceCenterZ, Heightmap.Type.WORLD_SURFACE_WG)
            : startPos.getY();

        int yAdjustment = pieceBoundingBox.minY + startPiece.getGroundLevelDelta(); // groundLevelDelta seems to always be 1. Not sure what the point of this is.
        startPiece.translate(0, pieceCenterY - yAdjustment, 0); // Ends up always offseting the piece by y = -1?

        components.add(startPiece); // Add start piece to list of pieces

        if (jigsawConfig.getMaxChainPieceLength() > 0) { // Realistically this should always be true. Why make a jigsaw config with a non-positive size?
            Box axisAlignedBB = new Box(pieceCenterX - 80, pieceCenterY - 80, pieceCenterZ - 80, pieceCenterX + 80 + 1, pieceCenterY + 80 + 1, pieceCenterZ + 80 + 1);
            Assembler assembler = new Assembler(jigsawPoolRegistry, jigsawConfig.getMaxChainPieceLength(), chunkGenerator, templateManager, components, random);
            Entry startPieceEntry = new Entry(
                startPiece,
                new MutableObject<>(
                    VoxelShapes.combineAndSimplify(
                        VoxelShapes.cuboid(axisAlignedBB),
                        VoxelShapes.cuboid(Box.from(pieceBoundingBox)),
                        BooleanBiFunction.ONLY_FIRST
                    )
                ),
                pieceCenterY + 80,
                0
            );
            assembler.availablePieces.addLast(startPieceEntry);

            while (!assembler.availablePieces.isEmpty()) {
                Entry entry = assembler.availablePieces.removeFirst();
                assembler.generatePiece(entry.piece, entry.pieceShape, entry.minY, entry.depth, doBoundaryAdjustments);
            }
        }
    }

    public static final class Assembler {
        private final Registry<StructurePool> poolRegistry;
        private final int maxDepth;
        private final ChunkGenerator chunkGenerator;
        private final StructureManager structureManager;
        private final List<? super PoolStructurePiece> structurePieces;
        private final Random rand;
        public final Deque<Entry> availablePieces = Queues.newArrayDeque();
        private final Map<Identifier, Integer> pieceCounts;
        private final int maxY;

        public Assembler(Registry<StructurePool> poolRegistry, int maxDepth, ChunkGenerator chunkGenerator, StructureManager structureManager, List<? super PoolStructurePiece> structurePieces, Random rand) {
            this.poolRegistry = poolRegistry;
            this.maxDepth = maxDepth;
            this.chunkGenerator = chunkGenerator;
            this.structureManager = structureManager;
            this.structurePieces = structurePieces;
            this.rand = rand;
            // Initialize piece counts
            this.pieceCounts = new HashMap<>();
            this.pieceCounts.put(new Identifier(BetterStrongholds.MOD_ID, "rooms/grand_library"), BetterStrongholds.CONFIG.betterStrongholds.pieceSettings.grandLibraryMaxCount);
            this.pieceCounts.put(new Identifier(BetterStrongholds.MOD_ID, "rooms/library_md"), BetterStrongholds.CONFIG.betterStrongholds.pieceSettings.smallLibraryMaxCount);
            this.pieceCounts.put(new Identifier(BetterStrongholds.MOD_ID, "rooms/prison_lg"), BetterStrongholds.CONFIG.betterStrongholds.pieceSettings.prisonMaxCount);
            this.pieceCounts.put(new Identifier(BetterStrongholds.MOD_ID, "rooms/cmd_acarii"), BetterStrongholds.CONFIG.betterStrongholds.pieceSettings.cmdAcariiMaxCount);
            this.pieceCounts.put(new Identifier(BetterStrongholds.MOD_ID, "rooms/cmd_yung"), BetterStrongholds.CONFIG.betterStrongholds.pieceSettings.cmdYungMaxCount);
            this.pieceCounts.put(new Identifier(BetterStrongholds.MOD_ID, "rooms/treasure_room_lg"), BetterStrongholds.CONFIG.betterStrongholds.pieceSettings.treasureRoomMaxCount);
            this.pieceCounts.put(new Identifier(BetterStrongholds.MOD_ID, "portal_rooms/portal_room"), BetterStrongholds.CONFIG.betterStrongholds.pieceSettings.portalRoomMaxCount);
            this.pieceCounts.put(new Identifier(BetterStrongholds.MOD_ID, "rooms/armoury_md"), BetterStrongholds.CONFIG.betterStrongholds.pieceSettings.armouryLargeRoomMaxCount);
            this.pieceCounts.put(new Identifier(BetterStrongholds.MOD_ID, "rooms/armoury_sm"), BetterStrongholds.CONFIG.betterStrongholds.pieceSettings.armourySmallRoomMaxCount);
            this.maxY = BetterStrongholds.CONFIG.betterStrongholds.general.strongholdMaxY;
        }

        public void generatePiece(PoolStructurePiece piece, MutableObject<VoxelShape> voxelShape, int minY, int depth, boolean doBoundaryAdjustments) {
            // Collect data from params regarding piece to process
            StructurePoolElement pieceBlueprint = piece.getPoolElement();
            BlockPos piecePos = piece.getPos();
            BlockRotation pieceRotation = piece.getRotation();
            BlockBox pieceBoundingBox = piece.getBoundingBox();
            int pieceMinY = pieceBoundingBox.minY;

            // I think this is a holder variable for reuse
            MutableObject<VoxelShape> tempNewPieceVoxelShape = new MutableObject<>();

            // Get list of all jigsaw blocks in this piece
            List<Structure.StructureBlockInfo> pieceJigsawBlocks = pieceBlueprint.getStructureBlockInfos(this.structureManager, piecePos, pieceRotation, this.rand);

            for (Structure.StructureBlockInfo jigsawBlock : pieceJigsawBlocks) {
                // Gather jigsaw block information
                Direction direction = JigsawBlock.getFacing(jigsawBlock.state);
                BlockPos jigsawBlockPos = jigsawBlock.pos;
                BlockPos jigsawBlockTargetPos = jigsawBlockPos.offset(direction);

                // Get the jigsaw block's piece pool
                Identifier jigsawBlockPool = new Identifier(jigsawBlock.tag.getString("pool"));
                Optional<StructurePool> poolOptional = this.poolRegistry.getOrEmpty(jigsawBlockPool);

                // Only continue if we are using the jigsaw pattern registry and if it is not empty
                if (!(poolOptional.isPresent() && (poolOptional.get().getElementCount() != 0 || Objects.equals(jigsawBlockPool, StructurePools.EMPTY.getValue())))) {
                    BetterStrongholds.LOGGER.warn("Empty or nonexistent pool: {}", jigsawBlockPool);
                    continue;
                }

                // Get the jigsaw block's fallback pool (which is a part of the pool's JSON)
                Identifier jigsawBlockFallback = poolOptional.get().getTerminatorsId();
                Optional<StructurePool> fallbackOptional = this.poolRegistry.getOrEmpty(jigsawBlockFallback);

                // Only continue if the fallback pool is present and valid
                if (!(fallbackOptional.isPresent() && (fallbackOptional.get().getElementCount() != 0 || Objects.equals(jigsawBlockFallback, StructurePools.EMPTY.getValue())))) {
                    BetterStrongholds.LOGGER.warn("Empty or nonexistent fallback pool: {}", jigsawBlockFallback);
                    continue;
                }

                // Adjustments for if the target block position is inside the current piece
                boolean isTargetInsideCurrentPiece = pieceBoundingBox.contains(jigsawBlockTargetPos);
                MutableObject<VoxelShape> pieceVoxelShape;
                int targetPieceBoundsTop;
                if (isTargetInsideCurrentPiece) {
                    pieceVoxelShape = tempNewPieceVoxelShape;
                    targetPieceBoundsTop = pieceMinY;
                    if (tempNewPieceVoxelShape.getValue() == null) {
                        tempNewPieceVoxelShape.setValue(VoxelShapes.cuboid(Box.from(pieceBoundingBox)));
                    }
                } else {
                    pieceVoxelShape = voxelShape;
                    targetPieceBoundsTop = minY;
                }

                // Process the pool pieces, randomly choosing different pieces from the pool to spawn
                if (depth != this.maxDepth) {
                    StructurePoolElement generatedPiece = this.processList(new ArrayList<>(poolOptional.get().elementCounts), doBoundaryAdjustments, jigsawBlock, jigsawBlockTargetPos, pieceMinY, jigsawBlockPos, pieceVoxelShape, piece, depth, targetPieceBoundsTop);
                    if (generatedPiece != null) continue; // Stop here since we've already generated the piece
                }

                // Process the fallback pieces in the event none of the pool pieces work
                this.processList(new ArrayList<>(fallbackOptional.get().elementCounts), doBoundaryAdjustments, jigsawBlock, jigsawBlockTargetPos, pieceMinY, jigsawBlockPos, pieceVoxelShape, piece, depth, targetPieceBoundsTop);
            }
        }

        /**
         * Helper function. Searches candidatePieces for a suitable piece to spawn.
         * All other params are intended to be passed directly from {@link Assembler#generatePiece}
         * @return The piece genereated, or null if no suitable pieces were found.
         */
        private StructurePoolElement processList(
            List<Pair<StructurePoolElement, Integer>> candidatePieces,
            boolean doBoundaryAdjustments,
            Structure.StructureBlockInfo jigsawBlock,
            BlockPos jigsawBlockTargetPos,
            int pieceMinY,
            BlockPos jigsawBlockPos,
            MutableObject<VoxelShape> pieceVoxelShape,
            PoolStructurePiece piece,
            int depth,
            int targetPieceBoundsTop
        ) {
            StructurePool.Projection piecePlacementBehavior = piece.getPoolElement().getProjection();
            boolean isPieceRigid = piecePlacementBehavior == StructurePool.Projection.RIGID;
            int jigsawBlockRelativeY = jigsawBlockPos.getY() - pieceMinY;
            int surfaceHeight = -1; // The y-coordinate of the surface. Only used if isPieceRigid is false.

            int totalCount = candidatePieces.stream().mapToInt(Pair::getSecond).reduce(0, Integer::sum);

            while (candidatePieces.size() > 0) {
                // Prioritize portal room if the following conditions are met:
                // 1. It's a potential candidate for this pool
                // 2. It hasn't already been placed
                // 3. We are at least (maxDepth/2) pieces away from the starting room.
                Pair<StructurePoolElement, Integer> chosenPiecePair = null;
                if (this.pieceCounts.get(new Identifier(BetterStrongholds.MOD_ID, "portal_rooms/portal_room")) > 0) { // Condition 2
                    for (int i = 0; i < candidatePieces.size(); i++) {
                        Pair<StructurePoolElement, Integer> candidatePiecePair = candidatePieces.get(i);
                        StructurePoolElement candidatePiece = candidatePiecePair.getFirst();
                        if (((SinglePoolElement) candidatePiece).field_24015.left().get().equals(new Identifier(BetterStrongholds.MOD_ID, "portal_rooms/portal_room"))) { // Condition 1
                            if (depth >= maxDepth / 2) { // Condition 3
                                // All conditions are met. Use portal room as chosen piece.
                                chosenPiecePair = candidatePiecePair;
                            } else {
                                // If not far enough from starting room, remove the portal room piece from the list
                                totalCount -= candidatePiecePair.getSecond();
                                candidatePieces.remove(candidatePiecePair);
                            }
                            break;
                        }
                    }
                }

                // Choose piece if portal room wasn't selected
                if (chosenPiecePair == null) {
                    int chosenWeight = rand.nextInt(totalCount) + 1;

                    for (Pair<StructurePoolElement, Integer> candidate : candidatePieces) {
                        chosenWeight -= candidate.getSecond();
                        if (chosenWeight <= 0) {
                            chosenPiecePair = candidate;
                            break;
                        }
                    }
                }

                StructurePoolElement candidatePiece = chosenPiecePair.getFirst();

                // Vanilla check. Not sure on the implications of this.
                if (candidatePiece == EmptyPoolElement.INSTANCE) {
                    return null;
                }

                // Before performing any logic, check to ensure we haven't reached the max number of instances of this piece.
                // This logic is my own additional logic - vanilla does not offer this behavior.
                Identifier pieceName = ((SinglePoolElement)candidatePiece).field_24015.left().get();
                if (this.pieceCounts.containsKey(pieceName)) {
                    if (this.pieceCounts.get(pieceName) <= 0) {
                        // Remove this piece from the list of candidates and retry.
                        totalCount -= chosenPiecePair.getSecond();
                        candidatePieces.remove(chosenPiecePair);
                        continue;
                    }
                }

                // Try different rotations to see which sides of the piece are fit to be the receiving end
                for (BlockRotation rotation : BlockRotation.randomRotationOrder(this.rand)) {
                    List<Structure.StructureBlockInfo> candidateJigsawBlocks = candidatePiece.getStructureBlockInfos(this.structureManager, BlockPos.ORIGIN, rotation, this.rand);
                    BlockBox tempCandidateBoundingBox = candidatePiece.getBoundingBox(this.structureManager, BlockPos.ORIGIN, rotation);

                    // Some sort of logic for setting the candidateHeightAdjustments var if doBoundaryAdjustments.
                    // Not sure on this - personally, I never enable doBoundaryAdjustments.
                    int candidateHeightAdjustments;
                    if (doBoundaryAdjustments && tempCandidateBoundingBox.getBlockCountY() <= 16) {
                        candidateHeightAdjustments = candidateJigsawBlocks.stream().mapToInt((pieceCandidateJigsawBlock) -> {
                            if (!tempCandidateBoundingBox.contains(pieceCandidateJigsawBlock.pos.offset(JigsawBlock.getFacing(pieceCandidateJigsawBlock.state)))) {
                                return 0;
                            } else {
                                Identifier candidateTargetPool = new Identifier(pieceCandidateJigsawBlock.tag.getString("pool"));
                                Optional<StructurePool> candidateTargetPoolOptional = this.poolRegistry.getOrEmpty(candidateTargetPool);
                                Optional<StructurePool> candidateTargetFallbackOptional = candidateTargetPoolOptional.flatMap((p_242843_1_) -> this.poolRegistry.getOrEmpty(p_242843_1_.getTerminatorsId()));
                                int tallestCandidateTargetPoolPieceHeight = candidateTargetPoolOptional.map((p_242842_1_) -> p_242842_1_.getHighestY(this.structureManager)).orElse(0);
                                int tallestCandidateTargetFallbackPieceHeight = candidateTargetFallbackOptional.map((p_242840_1_) -> p_242840_1_.getHighestY(this.structureManager)).orElse(0);
                                return Math.max(tallestCandidateTargetPoolPieceHeight, tallestCandidateTargetFallbackPieceHeight);
                            }
                        }).max().orElse(0);
                    } else {
                        candidateHeightAdjustments = 0;
                    }

                    // Check for each of the candidate's jigsaw blocks for a match
                    for (Structure.StructureBlockInfo candidateJigsawBlock : candidateJigsawBlocks) {
                        if (JigsawBlock.attachmentMatches(jigsawBlock, candidateJigsawBlock)) {
                            BlockPos candidateJigsawBlockPos = candidateJigsawBlock.pos;
                            BlockPos candidateJigsawBlockRelativePos = new BlockPos(jigsawBlockTargetPos.getX() - candidateJigsawBlockPos.getX(), jigsawBlockTargetPos.getY() - candidateJigsawBlockPos.getY(), jigsawBlockTargetPos.getZ() - candidateJigsawBlockPos.getZ());

                            // Get the bounding box for the piece, offset by the relative position difference
                            BlockBox candidateBoundingBox = candidatePiece.getBoundingBox(this.structureManager, candidateJigsawBlockRelativePos, rotation);

                            // Determine if candidate is rigid
                            StructurePool.Projection candidatePlacementBehavior = candidatePiece.getProjection();
                            boolean isCandidateRigid = candidatePlacementBehavior == StructurePool.Projection.RIGID;

                            // Determine how much the candidate jigsaw block is off in the y direction.
                            // This will be needed to offset the candidate piece so that the jigsaw blocks line up properly.
                            int candidateJigsawBlockRelativeY = candidateJigsawBlockPos.getY();
                            int candidateJigsawYOffsetNeeded = jigsawBlockRelativeY - candidateJigsawBlockRelativeY + JigsawBlock.getFacing(jigsawBlock.state).getOffsetY();

                            // Determine how much we need to offset the candidate piece itself in order to have the jigsaw blocks aligned.
                            // Depends on if the placement of both pieces is rigid or not
                            int adjustedCandidatePieceMinY;
                            if (isPieceRigid && isCandidateRigid) {
                                adjustedCandidatePieceMinY = pieceMinY + candidateJigsawYOffsetNeeded;
                            } else {
                                if (surfaceHeight == -1) {
                                    surfaceHeight = this.chunkGenerator.getHeightOnGround(jigsawBlockPos.getX(), jigsawBlockPos.getZ(), Heightmap.Type.WORLD_SURFACE_WG);
                                }

                                adjustedCandidatePieceMinY = surfaceHeight - candidateJigsawBlockRelativeY;
                            }
                            int candidatePieceYOffsetNeeded = adjustedCandidatePieceMinY - candidateBoundingBox.minY;

                            // Offset the candidate's bounding box by the necessary amount
                            BlockBox adjustedCandidateBoundingBox = candidateBoundingBox.offset(0, candidatePieceYOffsetNeeded, 0);

                            // Add this offset to the relative jigsaw block position as well
                            BlockPos adjustedCandidateJigsawBlockRelativePos = candidateJigsawBlockRelativePos.add(0, candidatePieceYOffsetNeeded, 0);

                            // Final adjustments to the bounding box.
                            if (candidateHeightAdjustments > 0) {
                                int k2 = Math.max(candidateHeightAdjustments + 1, adjustedCandidateBoundingBox.maxY - adjustedCandidateBoundingBox.minY);
                                adjustedCandidateBoundingBox.maxY = adjustedCandidateBoundingBox.minY + k2;
                            }

                            // Prevent pieces from spawning above max Y
                            if (adjustedCandidateBoundingBox.maxY > this.maxY) {
                                continue;
                            }

                            // Some sort of final boundary check before adding the new piece.
                            // Not sure why the candidate box is shrunk by 0.25.
                            if (!VoxelShapes.matchesAnywhere
                                (
                                    pieceVoxelShape.getValue(),
                                    VoxelShapes.cuboid(Box.from(adjustedCandidateBoundingBox).contract(0.25D)),
                                    BooleanBiFunction.ONLY_SECOND
                                )
                            ) {
                                pieceVoxelShape.setValue(
                                    VoxelShapes.combine(
                                        pieceVoxelShape.getValue(),
                                        VoxelShapes.cuboid(Box.from(adjustedCandidateBoundingBox)),
                                        BooleanBiFunction.ONLY_FIRST
                                    )
                                );

                                // Determine ground level delta for this new piece
                                int newPieceGroundLevelDelta = piece.getGroundLevelDelta();
                                int groundLevelDelta;
                                if (isCandidateRigid) {
                                    groundLevelDelta = newPieceGroundLevelDelta - candidateJigsawYOffsetNeeded;
                                } else {
                                    groundLevelDelta = candidatePiece.getGroundLevelDelta();
                                }

                                // Create new piece
                                PoolStructurePiece newPiece = new PoolStructurePiece(
                                    this.structureManager,
                                    candidatePiece,
                                    adjustedCandidateJigsawBlockRelativePos,
                                    groundLevelDelta,
                                    rotation,
                                    adjustedCandidateBoundingBox
                                );

                                // Determine actual y-value for the new jigsaw block
                                int candidateJigsawBlockY;
                                if (isPieceRigid) {
                                    candidateJigsawBlockY = pieceMinY + jigsawBlockRelativeY;
                                } else if (isCandidateRigid) {
                                    candidateJigsawBlockY = adjustedCandidatePieceMinY + candidateJigsawBlockRelativeY;
                                } else {
                                    if (surfaceHeight == -1) {
                                        surfaceHeight = this.chunkGenerator.getHeightOnGround(jigsawBlockPos.getX(), jigsawBlockPos.getZ(), Heightmap.Type.WORLD_SURFACE_WG);
                                    }

                                    candidateJigsawBlockY = surfaceHeight + candidateJigsawYOffsetNeeded / 2;
                                }

                                // Add the junction to the existing piece
                                piece.addJunction(
                                    new JigsawJunction(
                                        jigsawBlockTargetPos.getX(),
                                        candidateJigsawBlockY - jigsawBlockRelativeY + newPieceGroundLevelDelta,
                                        jigsawBlockTargetPos.getZ(),
                                        candidateJigsawYOffsetNeeded,
                                        candidatePlacementBehavior)
                                );

                                // Add the junction to the new piece
                                newPiece.addJunction(
                                    new JigsawJunction(
                                        jigsawBlockPos.getX(),
                                        candidateJigsawBlockY - candidateJigsawBlockRelativeY + groundLevelDelta,
                                        jigsawBlockPos.getZ(),
                                        -candidateJigsawYOffsetNeeded,
                                        piecePlacementBehavior)
                                );

                                // Add the piece
                                this.structurePieces.add(newPiece);
                                if (depth + 1 <= this.maxDepth) {
                                    this.availablePieces.addLast(new Entry(newPiece, pieceVoxelShape, targetPieceBoundsTop, depth + 1));
                                }
                                // Update piece count, if an entry exists for this piece
                                if (this.pieceCounts.containsKey(pieceName)) {
                                    this.pieceCounts.put(pieceName, this.pieceCounts.get(pieceName) - 1);
                                }
                                return candidatePiece;
                            }
                        }
                    }
                }
                totalCount -= chosenPiecePair.getSecond();
                candidatePieces.remove(chosenPiecePair);
            }
            return null;
        }
    }

    public static final class Entry {
        public final PoolStructurePiece piece;
        public final MutableObject<VoxelShape> pieceShape;
        public final int minY;
        public final int depth;

        public Entry(PoolStructurePiece piece, MutableObject<VoxelShape> pieceShape, int minY, int depth) {
            this.piece = piece;
            this.pieceShape = pieceShape;
            this.minY = minY;
            this.depth = depth;
        }
    }
}
