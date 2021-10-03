package com.yungnickyoung.minecraft.betterstrongholds.world.processor;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterstrongholds.init.BSModProcessors;
import com.yungnickyoung.minecraft.yungsapi.world.BlockSetSelector;
import com.yungnickyoung.minecraft.yungsapi.world.processor.ISafeWorldModifier;
import net.minecraft.block.*;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.SlabType;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;

import java.util.Optional;
import java.util.Random;

/**
 * Dynamically generates legs below the stronghold.
 */
public class LegProcessor extends StructureProcessor implements ISafeWorldModifier {
    public static final LegProcessor INSTANCE = new LegProcessor();
    public static final Codec<LegProcessor> CODEC = Codec.unit(() -> INSTANCE);

    private final BlockSetSelector stoneBrickSelector = new BlockSetSelector(Blocks.STONE_BRICKS.getDefaultState())
        .addBlock(Blocks.MOSSY_STONE_BRICKS.getDefaultState(), 0.3f)
        .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 0.2f)
        .addBlock(Blocks.INFESTED_STONE_BRICKS.getDefaultState(), 0.05f);

    @Override
    public Structure.StructureBlockInfo process(WorldView worldReader, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Structure.StructureBlockInfo blockInfoLocal, Structure.StructureBlockInfo blockInfoGlobal, StructurePlacementData structurePlacementData) {
        if (blockInfoGlobal.state.isOf(Blocks.YELLOW_STAINED_GLASS)) {
            ChunkPos currentChunkPos = new ChunkPos(blockInfoGlobal.pos);
            Chunk currentChunk = worldReader.getChunk(currentChunkPos.x, currentChunkPos.z);
            Random random = structurePlacementData.getRandom(blockInfoGlobal.pos);

            // Always replace the glass itself with stone bricks
            blockInfoGlobal = new Structure.StructureBlockInfo(blockInfoGlobal.pos, stoneBrickSelector.get(random), blockInfoGlobal.nbt);

            // Reusable mutable
            BlockPos.Mutable mutable = blockInfoGlobal.pos.down().mutableCopy(); // Move down since we already processed the first block

            // Chunk section information
            int sectionYIndex = currentChunk.getSectionIndex(mutable.getY());

            // Validate chunk section index. Sometimes the index is -1. Not sure why, but this will
            // at least prevent the game from crashing.
            if (sectionYIndex < 0) {
                return blockInfoGlobal;
            }

            ChunkSection currChunkSection = currentChunk.getSection(sectionYIndex);

            // Initialize currBlock
            Optional<BlockState> currBlock = getBlockStateSafe(currChunkSection, mutable);
            if (currBlock.isEmpty()) return blockInfoGlobal;

            int yBelow = 1;

            while (mutable.getY() > worldReader.getBottomY() && (currBlock.get().getMaterial() == Material.AIR || currBlock.get().getMaterial() == Material.WATER || currBlock.get().getMaterial() == Material.LAVA)) {
                // Place block in vertical pillar
                setBlockStateSafe(currChunkSection, mutable, stoneBrickSelector.get(random));

                // Generate rafters
                if (yBelow == 1) {
                    BlockPos.Mutable tempMutable;
                    for (Direction direction : Direction.Type.HORIZONTAL) {
                        tempMutable = mutable.offset(direction).mutableCopy();
                        setBlockStateSafe(
                            worldReader,
                            tempMutable,
                            Blocks.STONE_BRICK_STAIRS.getDefaultState()
                                .with(StairsBlock.HALF, BlockHalf.TOP)
                                .with(StairsBlock.FACING, direction.getOpposite())
                                .with(StairsBlock.WATERLOGGED, worldReader.getFluidState(tempMutable).isIn(FluidTags.WATER)));

                        tempMutable.move(direction);
                        setBlockStateSafe(
                            worldReader,
                            tempMutable,
                            Blocks.STONE_BRICK_STAIRS.getDefaultState()
                                .with(StairsBlock.HALF, BlockHalf.TOP)
                                .with(StairsBlock.FACING, direction)
                                .with(StairsBlock.WATERLOGGED, worldReader.getFluidState(tempMutable).isIn(FluidTags.WATER)));

                        // Middle piece between two adjacent pieces.
                        // Only place if there is another rafter adjacent
                        tempMutable.move(direction).move(Direction.UP);
                        Optional<BlockState> aboveBlockState = getBlockStateSafe(worldReader, tempMutable);
                        Block aboveBlock = aboveBlockState.isPresent() ? aboveBlockState.get().getBlock() : null;

                        tempMutable.move(direction).move(Direction.DOWN);
                        Optional<BlockState> belowBlockState = getBlockStateSafe(worldReader, tempMutable);
                        Block belowBlock = belowBlockState.isPresent() ? belowBlockState.get().getBlock() : null;

                        if (belowBlock == Blocks.STONE_BRICK_STAIRS || aboveBlock == Blocks.STONE_BRICKS || aboveBlock == Blocks.CRACKED_STONE_BRICKS || aboveBlock == Blocks.MOSSY_STONE_BRICKS || aboveBlock == Blocks.INFESTED_STONE_BRICKS) {
                            tempMutable.move(direction.getOpposite());
                            setBlockStateSafe(
                                worldReader,
                                tempMutable,
                                Blocks.STONE_BRICKS.getDefaultState());
                        }
                    }
                } else if (yBelow == 2) {
                    BlockPos.Mutable tempMutable;
                    for (Direction direction : Direction.Type.HORIZONTAL) {
                        tempMutable = mutable.offset(direction).mutableCopy();
                        setBlockStateSafe(
                            worldReader,
                            tempMutable,
                            Blocks.STONE_BRICK_STAIRS.getDefaultState()
                                .with(StairsBlock.HALF, BlockHalf.TOP)
                                .with(StairsBlock.FACING, direction.getOpposite())
                                .with(StairsBlock.WATERLOGGED, worldReader.getFluidState(tempMutable).isIn(FluidTags.WATER)));

                        tempMutable.move(direction);
                        setBlockStateSafe(
                            worldReader,
                            tempMutable,
                            Blocks.STONE_BRICK_SLAB.getDefaultState()
                                .with(SlabBlock.TYPE, SlabType.TOP)
                                .with(StairsBlock.WATERLOGGED, worldReader.getFluidState(tempMutable).isIn(FluidTags.WATER)));
                    }
                }

                // Move down
                mutable.move(Direction.DOWN);

                // Update index for new position
                sectionYIndex = currentChunk.getSectionIndex(mutable.getY());

                // Validate chunk section index. Sometimes the index is -1. Not sure why, but this will
                // at least prevent the game from crashing.
                if (sectionYIndex < 0) {
                    return blockInfoGlobal;
                }

                // Update chunk section for new position
                currChunkSection = currentChunk.getSection(sectionYIndex);
                currBlock = getBlockStateSafe(currChunkSection, mutable);
                if (currBlock.isEmpty()) break;
                yBelow++;
            }
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return BSModProcessors.LEG_PROCESSOR;
    }
}
