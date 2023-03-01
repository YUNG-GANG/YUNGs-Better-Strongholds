package com.yungnickyoung.minecraft.betterstrongholds.world.processor;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterstrongholds.module.StructureProcessorTypeModule;
import com.yungnickyoung.minecraft.yungsapi.world.structure.processor.ISafeWorldModifier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.Optional;

/**
 * A patchwork fix for https://bugs.mojang.com/browse/MC-130584.
 */
public class WaterloggedProcessor extends StructureProcessor implements ISafeWorldModifier {
    public static final WaterloggedProcessor INSTANCE = new WaterloggedProcessor();
    public static final Codec<WaterloggedProcessor> CODEC = Codec.unit(() -> INSTANCE);

    /**
     * Workaround for https://bugs.mojang.com/browse/MC-130584
     * Due to a hardcoded field in Templates, any waterloggable blocks in structures replacing water in the world will become waterlogged.
     * Idea of workaround is detect if we are placing a waterloggable block and if so, remove the water in the world instead.
     */
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                             StructureTemplate.StructureBlockInfo blockInfoLocal,
                                                             StructureTemplate.StructureBlockInfo blockInfoGlobal,
                                                             StructurePlaceSettings structurePlacementData) {
        // Check if block is waterloggable and not intended to be waterlogged
        if (blockInfoGlobal.state.hasProperty(BlockStateProperties.WATERLOGGED) && !blockInfoGlobal.state.getValue(BlockStateProperties.WATERLOGGED)) {
            ChunkPos currentChunkPos = new ChunkPos(blockInfoGlobal.pos);
            ChunkAccess currentChunk = levelReader.getChunk(currentChunkPos.x, currentChunkPos.z);
            int sectionYIndex = currentChunk.getSectionIndex(blockInfoGlobal.pos.getY());

            // Validate chunk section index. Sometimes the index is -1. Not sure why, but this will
            // at least prevent the game from crashing.
            if (sectionYIndex < 0) {
                return blockInfoGlobal;
            }

            LevelChunkSection currChunkSection = currentChunk.getSection(sectionYIndex);

            if (getFluidStateSafe(levelReader, blockInfoGlobal.pos).is(FluidTags.WATER)) {
                setBlockStateSafe(levelReader, blockInfoGlobal.pos, Blocks.STONE_BRICKS.defaultBlockState());
            }

            // Remove water in adjacent blocks
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
            for (Direction direction : Direction.values()) {
                mutable.set(blockInfoGlobal.pos).move(direction);
                if (currentChunkPos.x != mutable.getX() >> 4 || currentChunkPos.z != mutable.getZ() >> 4) {
                    currentChunkPos = new ChunkPos(mutable);
                    currentChunk = levelReader.getChunk(currentChunkPos.x, currentChunkPos.z);
                    sectionYIndex = currentChunk.getSectionIndex(mutable.getY());
                    if (sectionYIndex < 0) {
                        return blockInfoGlobal;
                    }
                    currChunkSection = currentChunk.getSection(sectionYIndex);
                }

                if (getFluidStateSafe(currChunkSection, mutable).is(FluidTags.WATER)) {
                    Optional<BlockState> blockState = getBlockStateSafe(currChunkSection, mutable);
                    if (blockState.isPresent() && !(blockState.get().hasProperty(BlockStateProperties.WATERLOGGED) && blockState.get().getValue(BlockStateProperties.WATERLOGGED))) {
                        setBlockStateSafe(currChunkSection, mutable, Blocks.STONE_BRICKS.defaultBlockState());
                    }
                }
            }
        }

        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorTypeModule.WATERLOGGED_PROCESSOR;
    }
}

