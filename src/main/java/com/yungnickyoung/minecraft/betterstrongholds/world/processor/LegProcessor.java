package com.yungnickyoung.minecraft.betterstrongholds.world.processor;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterstrongholds.init.BSModProcessors;
import com.yungnickyoung.minecraft.yungsapi.world.BlockSetSelector;
import com.yungnickyoung.minecraft.yungsapi.world.processor.ISafeWorldModifier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.material.Material;

import java.util.Optional;
import java.util.Random;

/**
 * Dynamically generates legs below the stronghold.
 */
public class LegProcessor extends StructureProcessor implements ISafeWorldModifier {
    public static final LegProcessor INSTANCE = new LegProcessor();
    public static final Codec<LegProcessor> CODEC = Codec.unit(() -> INSTANCE);

    private final BlockSetSelector stoneBrickSelector = new BlockSetSelector(Blocks.STONE_BRICKS.defaultBlockState())
        .addBlock(Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), 0.3f)
        .addBlock(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), 0.2f)
        .addBlock(Blocks.INFESTED_STONE_BRICKS.defaultBlockState(), 0.05f);

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                             StructureTemplate.StructureBlockInfo blockInfoLocal,
                                                             StructureTemplate.StructureBlockInfo blockInfoGlobal,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfoGlobal.state.is(Blocks.YELLOW_STAINED_GLASS)) {
            ChunkPos currentChunkPos = new ChunkPos(blockInfoGlobal.pos);
            ChunkAccess currentChunk = levelReader.getChunk(currentChunkPos.x, currentChunkPos.z);
            Random random = structurePlacementData.getRandom(blockInfoGlobal.pos);

            // Always replace the glass itself with stone bricks
            blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos, stoneBrickSelector.get(random), blockInfoGlobal.nbt);

            // Reusable mutable
            BlockPos.MutableBlockPos mutable = blockInfoGlobal.pos.below().mutable(); // Move down since we already processed the first block

            // Chunk section information
            int sectionYIndex = currentChunk.getSectionIndex(mutable.getY());

            // Validate chunk section index. Sometimes the index is -1. Not sure why, but this will
            // at least prevent the game from crashing.
            if (sectionYIndex < 0) {
                return blockInfoGlobal;
            }

            LevelChunkSection currChunkSection = currentChunk.getSection(sectionYIndex);

            // Initialize currBlock
            Optional<BlockState> currBlock = getBlockStateSafe(currChunkSection, mutable);
            if (currBlock.isEmpty()) return blockInfoGlobal;

            int yBelow = 1;

            while (mutable.getY() > levelReader.getMinBuildHeight() && (currBlock.get().getMaterial() == Material.AIR || currBlock.get().getMaterial() == Material.WATER || currBlock.get().getMaterial() == Material.LAVA)) {
                // Place block in vertical pillar
                setBlockStateSafe(currChunkSection, mutable, stoneBrickSelector.get(random));

                // Generate rafters
                if (yBelow == 1) {
                    BlockPos.MutableBlockPos tempMutable;
                    for (Direction direction : Direction.Plane.HORIZONTAL) {
                        tempMutable = mutable.relative(direction).mutable();
                        setBlockStateSafe(
                            levelReader,
                            tempMutable,
                            Blocks.STONE_BRICK_STAIRS.defaultBlockState()
                                .setValue(StairBlock.HALF, Half.TOP)
                                .setValue(StairBlock.FACING, direction.getOpposite())
                                .setValue(StairBlock.WATERLOGGED, levelReader.getFluidState(tempMutable).is(FluidTags.WATER)));

                        tempMutable.move(direction);
                        setBlockStateSafe(
                            levelReader,
                            tempMutable,
                            Blocks.STONE_BRICK_STAIRS.defaultBlockState()
                                .setValue(StairBlock.HALF, Half.TOP)
                                .setValue(StairBlock.FACING, direction)
                                .setValue(StairBlock.WATERLOGGED, levelReader.getFluidState(tempMutable).is(FluidTags.WATER)));

                        // Middle piece between two adjacent pieces.
                        // Only place if there is another rafter adjacent
                        tempMutable.move(direction).move(Direction.UP);
                        Optional<BlockState> aboveBlockState = getBlockStateSafe(levelReader, tempMutable);
                        Block aboveBlock = aboveBlockState.map(BlockBehaviour.BlockStateBase::getBlock).orElse(null);

                        tempMutable.move(direction).move(Direction.DOWN);
                        Optional<BlockState> belowBlockState = getBlockStateSafe(levelReader, tempMutable);
                        Block belowBlock = belowBlockState.map(BlockBehaviour.BlockStateBase::getBlock).orElse(null);

                        if (belowBlock == Blocks.STONE_BRICK_STAIRS || aboveBlock == Blocks.STONE_BRICKS || aboveBlock == Blocks.CRACKED_STONE_BRICKS || aboveBlock == Blocks.MOSSY_STONE_BRICKS || aboveBlock == Blocks.INFESTED_STONE_BRICKS) {
                            tempMutable.move(direction.getOpposite());
                            setBlockStateSafe(
                                levelReader,
                                tempMutable,
                                Blocks.STONE_BRICKS.defaultBlockState());
                        }
                    }
                } else if (yBelow == 2) {
                    BlockPos.MutableBlockPos tempMutable;
                    for (Direction direction : Direction.Plane.HORIZONTAL) {
                        tempMutable = mutable.relative(direction).mutable();
                        setBlockStateSafe(
                            levelReader,
                            tempMutable,
                            Blocks.STONE_BRICK_STAIRS.defaultBlockState()
                                .setValue(StairBlock.HALF, Half.TOP)
                                .setValue(StairBlock.FACING, direction.getOpposite())
                                .setValue(StairBlock.WATERLOGGED, levelReader.getFluidState(tempMutable).is(FluidTags.WATER)));

                        tempMutable.move(direction);
                        setBlockStateSafe(
                            levelReader,
                            tempMutable,
                            Blocks.STONE_BRICK_SLAB.defaultBlockState()
                                .setValue(SlabBlock.TYPE, SlabType.TOP)
                                .setValue(SlabBlock.WATERLOGGED, levelReader.getFluidState(tempMutable).is(FluidTags.WATER)));
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
