package com.yungnickyoung.minecraft.betterstrongholds.world.processor;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholdsCommon;
import com.yungnickyoung.minecraft.betterstrongholds.module.StructureProcessorModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.Random;

/**
 * Replaces white stained glass and gray stained glass with cobwebs and air.
 * The replacement rate for each of these blocks is configurable.
 * By default, gray stained glass yields a higher proportion of cobwebs than white.
 * Gray is intended for use around mob spawners and similar hostile areas.
 */
public class CobwebProcessor extends StructureProcessor {
    public static final CobwebProcessor INSTANCE = new CobwebProcessor();
    public static final Codec<CobwebProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                             StructureTemplate.StructureBlockInfo blockInfoLocal,
                                                             StructureTemplate.StructureBlockInfo blockInfoGlobal,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfoGlobal.state.is(Blocks.TRIPWIRE) || blockInfoGlobal.state.is(Blocks.COBWEB)) {
            Random random = structurePlacementData.getRandom(blockInfoGlobal.pos);
            double replacementChance = getReplacementChance(blockInfoGlobal.state);
            if (random.nextDouble() < replacementChance)
                blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos, Blocks.COBWEB.defaultBlockState(), blockInfoGlobal.nbt);
            else
                blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos, Blocks.AIR.defaultBlockState(), blockInfoGlobal.nbt);
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorModule.COBWEB_PROCESSOR;
    }

    /**
     * Returns cobweb replacement chance for the given BlockState.
     */
    private double getReplacementChance(BlockState blockState) {
        if (blockState.is(Blocks.TRIPWIRE))
            return BetterStrongholdsCommon.CONFIG.general.cobwebReplacementChanceNormal;
        if (blockState.is(Blocks.COBWEB))
            return BetterStrongholdsCommon.CONFIG.general.cobwebReplacementChanceSpawner;
        else return 0; // Should never happen
    }
}
