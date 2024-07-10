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
public class RuinProcessor extends StructureProcessor implements ISafeWorldModifier {
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
                                                StructureTemplate.StructureBlockInfo blockInfoLocal,
                                                StructureTemplate.StructureBlockInfo blockInfoGlobal,
                                                StructurePlaceSettings structurePlacementData) {
        if (!BetterStrongholdsCommon.CONFIG.general.enableStructureRuin) {
            return blockInfoGlobal;
        }
        if (!(levelReader instanceof WorldGenRegion worldGenRegion)) {
            return blockInfoGlobal;
        }
        if (!safeBlocks.contains(blockInfoGlobal.state()) && worldGenRegion.getChunk(blockInfoGlobal.pos()).getBlockState(blockInfoGlobal.pos()).isAir()) {
            blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos(), Blocks.AIR.defaultBlockState(), blockInfoGlobal.nbt());
        }

        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorTypeModule.RUIN_PROCESSOR;
    }
}
