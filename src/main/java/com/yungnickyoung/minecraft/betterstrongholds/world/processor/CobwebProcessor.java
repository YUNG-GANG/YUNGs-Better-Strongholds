package com.yungnickyoung.minecraft.betterstrongholds.world.processor;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholds;
import com.yungnickyoung.minecraft.betterstrongholds.init.BSModProcessors;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

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
    public Structure.StructureBlockInfo process(WorldView worldReader, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Structure.StructureBlockInfo blockInfoLocal, Structure.StructureBlockInfo blockInfoGlobal, StructurePlacementData structurePlacementData) {
        if (blockInfoGlobal.state.isOf(Blocks.WHITE_STAINED_GLASS) || blockInfoGlobal.state.isOf(Blocks.GRAY_STAINED_GLASS)) {
            Random random = structurePlacementData.getRandom(blockInfoGlobal.pos);
            double replacementChance = getReplacementChance(blockInfoGlobal.state);
            if (random.nextDouble() < replacementChance)
                blockInfoGlobal = new Structure.StructureBlockInfo(blockInfoGlobal.pos, Blocks.COBWEB.getDefaultState(), blockInfoGlobal.tag);
            else
                blockInfoGlobal = new Structure.StructureBlockInfo(blockInfoGlobal.pos, Blocks.AIR.getDefaultState(), blockInfoGlobal.tag);
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return BSModProcessors.COBWEB_PROCESSOR;
    }

    /**
     * Returns cobweb replacement chance for the given BlockState.
     */
    private double getReplacementChance(BlockState blockState) {
        if (blockState.isOf(Blocks.WHITE_STAINED_GLASS))
            return BetterStrongholds.CONFIG.betterStrongholds.general.cobwebReplacementChanceNormal;
        if (blockState.isOf(Blocks.GRAY_STAINED_GLASS))
            return BetterStrongholds.CONFIG.betterStrongholds.general.cobwebReplacementChanceSpawner;
        else return 0; // Should never happen
    }
}
