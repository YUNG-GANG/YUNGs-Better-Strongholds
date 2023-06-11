package com.yungnickyoung.minecraft.betterstrongholds.world.processor;

import com.mojang.serialization.Codec;
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
public class EndPortalFrameProcessor extends StructureProcessor {
    public static final EndPortalFrameProcessor INSTANCE = new EndPortalFrameProcessor();
    public static final Codec<EndPortalFrameProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                             StructureTemplate.StructureBlockInfo blockInfoLocal,
                                                             StructureTemplate.StructureBlockInfo blockInfoGlobal,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfoGlobal.state().is(Blocks.END_PORTAL_FRAME)) {
            RandomSource randomSource = structurePlacementData.getRandom(blockInfoGlobal.pos());
            if (randomSource.nextFloat() < 0.1f)
                blockInfoGlobal = new StructureTemplate.StructureBlockInfo(
                        blockInfoGlobal.pos(),
                        blockInfoGlobal.state().setValue(EndPortalFrameBlock.HAS_EYE, true),
                        blockInfoGlobal.nbt());
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorTypeModule.END_PORTAL_FRAME_PROCESSOR;
    }
}