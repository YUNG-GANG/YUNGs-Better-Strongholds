package com.yungnickyoung.minecraft.betterstrongholds.world.processor;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholds;
import com.yungnickyoung.minecraft.betterstrongholds.init.BSModProcessors;
import net.minecraft.block.Blocks;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

import java.util.Random;

/**
 * Replaces some lanterns with air, making the structure look more weathered and less complete.
 */
public class LanternProcessor extends StructureProcessor {
    public static final LanternProcessor INSTANCE = new LanternProcessor();
    public static final Codec<LanternProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public Structure.StructureBlockInfo process(WorldView worldReader, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Structure.StructureBlockInfo blockInfoLocal, Structure.StructureBlockInfo blockInfoGlobal, StructurePlacementData structurePlacementData) {
        if (blockInfoGlobal.state.isOf(Blocks.LANTERN)) {
            Random random = structurePlacementData.getRandom(blockInfoGlobal.pos);
            double replacementChance = getReplacementChance();
            if (random.nextDouble() > replacementChance)
                blockInfoGlobal = new Structure.StructureBlockInfo(blockInfoGlobal.pos, Blocks.AIR.getDefaultState(), blockInfoGlobal.tag);
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return BSModProcessors.LANTERN_PROCESSOR;
    }

    /**
     * Returns lantern replacement chance for the given BlockState.
     */
    private double getReplacementChance() {
        return BetterStrongholds.CONFIG.betterStrongholds.general.lanternSpawnRate;
    }
}
