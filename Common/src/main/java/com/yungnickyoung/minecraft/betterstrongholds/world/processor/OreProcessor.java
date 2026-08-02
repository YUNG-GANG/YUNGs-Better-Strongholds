package com.yungnickyoung.minecraft.betterstrongholds.world.processor;

import com.mojang.serialization.MapCodec;
import com.yungnickyoung.minecraft.betterstrongholds.module.StructureProcessorTypeModule;
import com.yungnickyoung.minecraft.betterstrongholds.world.OreChances;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

/**
 * Replaces Nether gold ore blocks with a random ore.
 * The chance of a given ore being chosen is determined by the config.
 */
public class OreProcessor implements StructureProcessor {
    public static final OreProcessor INSTANCE = new OreProcessor();
    public static final MapCodec<OreProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                             BlockPos templateRelativePos,
                                                             StructureTemplate.StructureBlockInfo blockInfoGlobal,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfoGlobal.state().getBlock() == Blocks.NETHER_GOLD_ORE) {
            RandomSource randomSource = structurePlacementData.getRandom(blockInfoGlobal.pos());
            // Randomly select ore from list
            BlockState oreBlock = OreChances.get().getRandomOre(randomSource);
            blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos(), oreBlock, null);
        }
        return blockInfoGlobal;
    }

    public MapCodec<? extends StructureProcessor> codec() {
        return StructureProcessorTypeModule.ORE_PROCESSOR;
    }
}
