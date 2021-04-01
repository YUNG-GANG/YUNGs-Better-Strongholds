package com.yungnickyoung.minecraft.betterstrongholds.world.processor;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterstrongholds.init.ModProcessors;
import com.yungnickyoung.minecraft.yungsapi.world.BlockSetSelector;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.SlabType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

/**
 * Dynamically generates legs below the stronghold.
 */
@MethodsReturnNonnullByDefault
public class LegProcessor extends StructureProcessor {
    public static final LegProcessor INSTANCE = new LegProcessor();
    public static final Codec<LegProcessor> CODEC = Codec.unit(() -> INSTANCE);

    private BlockSetSelector stoneBrickSelector = new BlockSetSelector(Blocks.STONE_BRICKS.getDefaultState())
        .addBlock(Blocks.MOSSY_STONE_BRICKS.getDefaultState(), 0.3f)
        .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 0.2f)
        .addBlock(Blocks.INFESTED_STONE_BRICKS.getDefaultState(), 0.05f);

    @ParametersAreNonnullByDefault
    @Override
    public Template.BlockInfo process(IWorldReader worldReader, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Template.BlockInfo blockInfoLocal, Template.BlockInfo blockInfoGlobal, PlacementSettings structurePlacementData, @Nullable Template template) {
        if (blockInfoGlobal.state.isIn(Blocks.YELLOW_STAINED_GLASS)) {
            ChunkPos currentChunkPos = new ChunkPos(blockInfoGlobal.pos);
            IChunk currentChunk = worldReader.getChunk(currentChunkPos.x, currentChunkPos.z);
            Random random = structurePlacementData.getRandom(blockInfoGlobal.pos);
            BlockState randomBlock;

            // Always replace the glass itself with stone bricks
            randomBlock = stoneBrickSelector.get(random);
            currentChunk.setBlockState(blockInfoGlobal.pos, randomBlock, false);
            blockInfoGlobal = new Template.BlockInfo(blockInfoGlobal.pos, randomBlock, blockInfoGlobal.nbt);

            // Straight line down
            BlockPos.Mutable mutable = blockInfoGlobal.pos.down().toMutable();
            BlockState currBlock = worldReader.getBlockState(mutable);
            int yBelow = 1;

            while (mutable.getY() > 0 && (currBlock.getMaterial() == Material.AIR || currBlock.getMaterial() == Material.WATER || currBlock.getMaterial() == Material.LAVA)) {
                // Generate vertical pillar
                randomBlock = stoneBrickSelector.get(random);
                currentChunk.setBlockState(mutable, randomBlock, false);

                // Generate rafters
                if (yBelow == 1) {
                    BlockPos.Mutable tempMutable;
                    for (Direction direction : Direction.Plane.HORIZONTAL) {
                        tempMutable = mutable.offset(direction).toMutable();
                        worldReader.getChunk(tempMutable).setBlockState(
                            tempMutable,
                            Blocks.STONE_BRICK_STAIRS.getDefaultState()
                                .with(StairsBlock.HALF, Half.TOP)
                                .with(StairsBlock.FACING, direction.getOpposite())
                                .with(StairsBlock.WATERLOGGED, worldReader.getFluidState(tempMutable).isTagged(FluidTags.WATER)),
                            false);

                        tempMutable.move(direction);
                        worldReader.getChunk(tempMutable).setBlockState(
                            tempMutable,
                            Blocks.STONE_BRICK_STAIRS.getDefaultState()
                                .with(StairsBlock.HALF, Half.TOP)
                                .with(StairsBlock.FACING, direction)
                                .with(StairsBlock.WATERLOGGED, worldReader.getFluidState(tempMutable).isTagged(FluidTags.WATER)),
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
                    for (Direction direction : Direction.Plane.HORIZONTAL) {
                        tempMutable = mutable.offset(direction).toMutable();
                        worldReader.getChunk(tempMutable).setBlockState(
                            tempMutable,
                            Blocks.STONE_BRICK_STAIRS.getDefaultState()
                                .with(StairsBlock.HALF, Half.TOP)
                                .with(StairsBlock.FACING, direction.getOpposite())
                                .with(StairsBlock.WATERLOGGED, worldReader.getFluidState(tempMutable).isTagged(FluidTags.WATER)),
                            false);

                        tempMutable.move(direction);
                        worldReader.getChunk(tempMutable).setBlockState(
                            tempMutable,
                            Blocks.STONE_BRICK_SLAB.getDefaultState()
                                .with(SlabBlock.TYPE, SlabType.TOP)
                                .with(StairsBlock.WATERLOGGED, worldReader.getFluidState(tempMutable).isTagged(FluidTags.WATER)),
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

    protected IStructureProcessorType<?> getType() {
        return ModProcessors.LEG_PROCESSOR;
    }
}
