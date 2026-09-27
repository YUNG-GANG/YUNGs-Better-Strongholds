package com.yungnickyoung.minecraft.betterstrongholds.world.processor;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholdsCommon;
import com.yungnickyoung.minecraft.betterstrongholds.module.StructureProcessorTypeModule;
import com.yungnickyoung.minecraft.yungsapi.world.structure.processor.ISafeWorldModifier;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Replaces blocks with air where air exists in the world already.
 * Intended to give walls and other pieces a ruined appearance that opens up the structure to caves.
 */
public class RuinProcessor implements StructureProcessor, ISafeWorldModifier {
    public static final MapCodec<RuinProcessor> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    BlockState.CODEC.listOf().optionalFieldOf("safe_blocks", new ArrayList<>()).forGetter(config -> config.safeBlocks))
            .apply(instance, instance.stable(RuinProcessor::new)));

    public final List<BlockState> safeBlocks;

    private RuinProcessor(List<BlockState> safeBlocks) {
        this.safeBlocks = safeBlocks;
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                BlockPos jigsawPiecePos,
                                                BlockPos jigsawPieceBottomCenterPos,
                                                BlockPos pivotPos,
                                                StructureTemplate.StructureBlockInfo blockInfo,
                                                StructurePlaceSettings structurePlacementData) {
        if (!BetterStrongholdsCommon.CONFIG.general.enableStructureRuin) {
            return blockInfo;
        }
        if (!(levelReader instanceof WorldGenRegion worldGenRegion)) {
            return blockInfo;
        }
        if (!safeBlocks.contains(blockInfo.state()) && worldGenRegion.getChunk(blockInfo.pos()).getBlockState(blockInfo.pos()).isAir()) {
            blockInfo = new StructureTemplate.StructureBlockInfo(blockInfo.pos(), Blocks.AIR.defaultBlockState(), null);
        }

        return blockInfo;
    }

    @Override
    public MapCodec<? extends StructureProcessor> codec() {
        return StructureProcessorTypeModule.RUIN_PROCESSOR;
    }
}
