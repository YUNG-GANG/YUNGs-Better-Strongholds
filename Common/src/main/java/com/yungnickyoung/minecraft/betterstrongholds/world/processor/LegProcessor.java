package com.yungnickyoung.minecraft.betterstrongholds.world.processor;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterstrongholds.module.StructureProcessorTypeModule;
import com.yungnickyoung.minecraft.yungsapi.api.world.randomize.BlockStateRandomizer;
import com.yungnickyoung.minecraft.yungsapi.world.structure.processor.ISafeWorldModifier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.Optional;

/**
 * Dynamically generates legs below the stronghold.
 */
public class LegProcessor extends StructureProcessor implements ISafeWorldModifier {
    public static final LegProcessor INSTANCE = new LegProcessor();
    public static final Codec<LegProcessor> CODEC = Codec.unit(() -> INSTANCE);

    private final BlockStateRandomizer stoneBrickSelector = new BlockStateRandomizer(Blocks.STONE_BRICKS.defaultBlockState())
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
        if (blockInfoGlobal.state().is(Blocks.YELLOW_STAINED_GLASS) || blockInfoGlobal.state().is(Blocks.ORANGE_STAINED_GLASS)) {
            if (levelReader instanceof WorldGenRegion worldGenRegion && !worldGenRegion.getCenter().equals(new ChunkPos(blockInfoGlobal.pos()))) {
                return blockInfoGlobal;
            }

            RandomSource randomSource = structurePlacementData.getRandom(blockInfoGlobal.pos());

            // Replace the glass itself
            blockInfoGlobal = blockInfoGlobal.state().is(Blocks.YELLOW_STAINED_GLASS)
                    ? new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos(), stoneBrickSelector.get(randomSource), blockInfoGlobal.nbt())
                    : new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos(), Blocks.CYAN_TERRACOTTA.defaultBlockState(), blockInfoGlobal.nbt());

            // Reusable mutable
            BlockPos.MutableBlockPos mutable = blockInfoGlobal.pos().mutable().move(Direction.DOWN); // Move down since we already processed the first block
            BlockState currBlockState = levelReader.getBlockState(mutable);

            int yBelow = 1;

            while (mutable.getY() > levelReader.getMinBuildHeight()
                    && mutable.getY() < levelReader.getMaxBuildHeight()
                    && (currBlockState.isAir() || !levelReader.getFluidState(mutable).isEmpty())) {
                // Place block in vertical pillar
                levelReader.getChunk(mutable).setBlockState(mutable, stoneBrickSelector.get(randomSource), false);

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
                currBlockState = levelReader.getBlockState(mutable);
                yBelow++;
            }
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorTypeModule.LEG_PROCESSOR;
    }
}
