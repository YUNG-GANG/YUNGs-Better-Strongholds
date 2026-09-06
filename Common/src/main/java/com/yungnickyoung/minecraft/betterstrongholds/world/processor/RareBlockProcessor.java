package com.yungnickyoung.minecraft.betterstrongholds.world.processor;

import com.mojang.serialization.MapCodec;
import com.yungnickyoung.minecraft.betterstrongholds.module.StructureProcessorTypeModule;
import com.yungnickyoung.minecraft.betterstrongholds.world.RareBlockChances;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

/**
 * Replaces purpur blocks with a random rare block
 * The chance of a given block being chosen is determined by the config.
 */
public class RareBlockProcessor implements StructureProcessor {
    public static final RareBlockProcessor INSTANCE = new RareBlockProcessor();
    public static final MapCodec<RareBlockProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                             BlockPos pivotPos,
                                                             StructureTemplate.StructureBlockInfo blockInfo,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfo.state().getBlock() == Blocks.PURPUR_BLOCK) {
            RandomSource randomSource = structurePlacementData.getRandom(blockInfo.pos());
            // Randomly select ore from list
            BlockState rareBlock = RareBlockChances.get().getRandomRareBlock(randomSource);
            blockInfo = new StructureTemplate.StructureBlockInfo(blockInfo.pos(), rareBlock, null);
        }
        return blockInfo;
    }

    @Override
    public MapCodec<? extends StructureProcessor> codec() {
        return StructureProcessorTypeModule.RARE_BLOCK_PROCESSOR;
    }
}

