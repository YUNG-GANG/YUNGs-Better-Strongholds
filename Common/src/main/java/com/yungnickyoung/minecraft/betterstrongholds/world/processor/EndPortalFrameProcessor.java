package com.yungnickyoung.minecraft.betterstrongholds.world.processor;

import com.mojang.serialization.MapCodec;
import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholdsCommon;
import com.yungnickyoung.minecraft.betterstrongholds.module.StructureProcessorTypeModule;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EndPortalFrameBlock;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

/**
 * Randomly fills some end portal frames w/ eyes of ender.
 */
public class EndPortalFrameProcessor implements StructureProcessor {
    public static final EndPortalFrameProcessor INSTANCE = new EndPortalFrameProcessor();
    public static final MapCodec<EndPortalFrameProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                             BlockPos pivotPos,
                                                             StructureTemplate.StructureBlockInfo blockInfo,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfo.state().is(Blocks.END_PORTAL_FRAME)) {
            RandomSource randomSource = structurePlacementData.getRandom(blockInfo.pos());
            if (randomSource.nextFloat() < BetterStrongholdsCommon.CONFIG.general.filledPortalFrameChance)
                blockInfo = new StructureTemplate.StructureBlockInfo(
                        blockInfo.pos(),
                        blockInfo.state().setValue(EndPortalFrameBlock.HAS_EYE, true),
                        blockInfo.nbt());
        }
        return blockInfo;
    }

    @Override
    public MapCodec<? extends StructureProcessor> codec() {
        return StructureProcessorTypeModule.END_PORTAL_FRAME_PROCESSOR;
    }
}