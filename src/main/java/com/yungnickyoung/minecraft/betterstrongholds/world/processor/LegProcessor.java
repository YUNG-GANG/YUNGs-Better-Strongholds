package com.yungnickyoung.minecraft.betterstrongholds.world.processor;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterstrongholds.init.BSModProcessors;
import com.yungnickyoung.minecraft.yungsapi.world.BlockSetSelector;
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

import java.util.Random;

/**
 * Dynamically generates legs below the stronghold.
 */
public class LegProcessor extends StructureProcessor {
    public static final LegProcessor INSTANCE = new LegProcessor();
    public static final Codec<LegProcessor> CODEC = Codec.unit(() -> INSTANCE);

    private BlockSetSelector stoneBrickSelector = new BlockSetSelector(Blocks.STONE_BRICKS.getDefaultState())
        .addBlock(Blocks.MOSSY_STONE_BRICKS.getDefaultState(), 0.3f)
        .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 0.2f)
        .addBlock(Blocks.INFESTED_STONE_BRICKS.getDefaultState(), 0.05f);

    @Override
    public Structure.StructureBlockInfo process(WorldView worldReader, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Structure.StructureBlockInfo blockInfoLocal, Structure.StructureBlockInfo blockInfoGlobal, StructurePlacementData structurePlacementData) {
        if (blockInfoGlobal.state.isOf(Blocks.YELLOW_STAINED_GLASS)) {
            ChunkPos currentChunkPos = new ChunkPos(blockInfoGlobal.pos);
            Chunk currentChunk = worldReader.getChunk(currentChunkPos.x, currentChunkPos.z);
            Random random = structurePlacementData.getRandom(blockInfoGlobal.pos);
            BlockState randomBlock;

            // Always replace the glass itself with stone bricks
            randomBlock = stoneBrickSelector.get(random);
            currentChunk.setBlockState(blockInfoGlobal.pos, randomBlock, false);
            blockInfoGlobal = new Structure.StructureBlockInfo(blockInfoGlobal.pos, randomBlock, blockInfoGlobal.tag);

            // Straight line down
            BlockPos.Mutable mutable = blockInfoGlobal.pos.down().mutableCopy();
            BlockState currBlock = worldReader.getBlockState(mutable);
            int yBelow = 1;

            while (mutable.getY() > 0 && (currBlock.getMaterial() == Material.AIR || currBlock.getMaterial() == Material.WATER || currBlock.getMaterial() == Material.LAVA)) {
                // Generate vertical pillar
                randomBlock = stoneBrickSelector.get(random);
                currentChunk.setBlockState(mutable, randomBlock, false);

                // Generate rafters
                if (yBelow == 1) {
                    BlockPos.Mutable tempMutable;
                    for (Direction direction : Direction.Type.HORIZONTAL) {
                        tempMutable = mutable.offset(direction).mutableCopy();
                        worldReader.getChunk(tempMutable).setBlockState(
                            tempMutable,
                            Blocks.STONE_BRICK_STAIRS.getDefaultState()
                                .with(StairsBlock.HALF, BlockHalf.TOP)
                                .with(StairsBlock.FACING, direction.getOpposite())
                                .with(StairsBlock.WATERLOGGED, worldReader.getFluidState(tempMutable).isIn(FluidTags.WATER)),
                            false);

                        tempMutable.move(direction);
                        worldReader.getChunk(tempMutable).setBlockState(
                            tempMutable,
                            Blocks.STONE_BRICK_STAIRS.getDefaultState()
                                .with(StairsBlock.HALF, BlockHalf.TOP)
                                .with(StairsBlock.FACING, direction)
                                .with(StairsBlock.WATERLOGGED, worldReader.getFluidState(tempMutable).isIn(FluidTags.WATER)),
                            false);

                        // Middle piece between two adjacent pieces.
                        // Only place if there is another rafter adjacent
                        tempMutable.move(direction).move(Direction.UP);
                        Block aboveBlock = worldReader.getBlockState(tempMutable).getBlock();
                        tempMutable.move(direction).move(Direction.DOWN);
                        if (worldReader.getBlockState(tempMutable).getBlock() == Blocks.STONE_BRICK_STAIRS || aboveBlock == Blocks.STONE_BRICKS || aboveBlock == Blocks.CRACKED_STONE_BRICKS || aboveBlock == Blocks.MOSSY_STONE_BRICKS || aboveBlock == Blocks.INFESTED_STONE_BRICKS) {
                            tempMutable.move(direction.getOpposite());
                            worldReader.getChunk(tempMutable).setBlockState(
                                tempMutable,
                                Blocks.STONE_BRICKS.getDefaultState(),
                                false);
                        }
                    }
                } else if (yBelow == 2) {
                    BlockPos.Mutable tempMutable;
                    for (Direction direction : Direction.Type.HORIZONTAL) {
                        tempMutable = mutable.offset(direction).mutableCopy();
                        worldReader.getChunk(tempMutable).setBlockState(
                            tempMutable,
                            Blocks.STONE_BRICK_STAIRS.getDefaultState()
                                .with(StairsBlock.HALF, BlockHalf.TOP)
                                .with(StairsBlock.FACING, direction.getOpposite())
                                .with(StairsBlock.WATERLOGGED, worldReader.getFluidState(tempMutable).isIn(FluidTags.WATER)),
                            false);

                        tempMutable.move(direction);
                        worldReader.getChunk(tempMutable).setBlockState(
                            tempMutable,
                            Blocks.STONE_BRICK_SLAB.getDefaultState()
                                .with(SlabBlock.TYPE, SlabType.TOP)
                                .with(StairsBlock.WATERLOGGED, worldReader.getFluidState(tempMutable).isIn(FluidTags.WATER)),
                            false);
                    }
                }

                mutable.move(Direction.DOWN);
                currBlock = worldReader.getBlockState(mutable);
                yBelow++;
            }
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return BSModProcessors.LEG_PROCESSOR;
    }
}
