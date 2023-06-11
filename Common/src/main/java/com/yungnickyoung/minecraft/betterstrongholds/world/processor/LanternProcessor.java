package com.yungnickyoung.minecraft.betterstrongholds.world.processor;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholdsCommon;
import com.yungnickyoung.minecraft.betterstrongholds.module.StructureProcessorTypeModule;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

/**
 * Replaces some lanterns with air, making the structure look more weathered and less complete.
 */
public class LanternProcessor extends StructureProcessor {
    public static final LanternProcessor INSTANCE = new LanternProcessor();
    public static final Codec<LanternProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                             StructureTemplate.StructureBlockInfo blockInfoLocal,
                                                             StructureTemplate.StructureBlockInfo blockInfoGlobal,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfoGlobal.state().is(Blocks.LANTERN)) {
            RandomSource randomSource = structurePlacementData.getRandom(blockInfoGlobal.pos());
            double replacementChance = getReplacementChance();
            if (randomSource.nextDouble() > replacementChance)
                blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos(), Blocks.AIR.defaultBlockState(), blockInfoGlobal.nbt());
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorTypeModule.LANTERN_PROCESSOR;
    }

    /**
     * Returns lantern replacement chance for the given BlockState.
     */
    private double getReplacementChance() {
        return BetterStrongholdsCommon.CONFIG.general.lanternSpawnRate;
    }
}
