package com.yungnickyoung.minecraft.betterstrongholds.world.processor;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yungnickyoung.minecraft.betterstrongholds.module.StructureProcessorTypeModule;
import com.yungnickyoung.minecraft.yungsapi.world.structure.processor.ISafeWorldModifier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.Optional;

/**
 * Ensures redstone doesn't spawn floating in the air.
 */
public class RedstoneProcessor implements StructureProcessor, ISafeWorldModifier {
    public static final RedstoneProcessor INSTANCE = new RedstoneProcessor(Blocks.STONE_BRICKS);
    public static final MapCodec<RedstoneProcessor> CODEC = RecordCodecBuilder.mapCodec(codecBuilder -> codecBuilder
            .group(
                    BuiltInRegistries.BLOCK.byNameCodec()
                            .fieldOf("below_block")
                            .orElse(Blocks.STONE_BRICKS)
                            .forGetter(processor -> processor.belowBlock))
            .apply(codecBuilder, codecBuilder.stable(RedstoneProcessor::new)));

    private RedstoneProcessor(Block belowBlock) {
        this.belowBlock = belowBlock;
    }

    private final Block belowBlock;

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                             BlockPos pivotPos,
                                                             StructureTemplate.StructureBlockInfo blockInfo,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfo.state().is(Blocks.REDSTONE_WIRE)) {
            Optional<BlockState> belowBlockState = getBlockStateSafe(levelReader, blockInfo.pos().below());
            if (belowBlockState.isEmpty() || !belowBlockState.get().isFaceSturdy(levelReader, blockInfo.pos().below(), Direction.UP)) {
                setBlockStateSafe(levelReader, blockInfo.pos().below(), belowBlock.defaultBlockState());
            }
        }
        return blockInfo;
    }

    @Override
    public MapCodec<? extends StructureProcessor> codec() {
        return StructureProcessorTypeModule.REDSTONE_PROCESSOR;
    }
}
