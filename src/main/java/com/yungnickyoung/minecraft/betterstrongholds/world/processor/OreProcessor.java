package com.yungnickyoung.minecraft.betterstrongholds.world.processor;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterstrongholds.init.BSModProcessors;
import com.yungnickyoung.minecraft.betterstrongholds.world.OreChances;
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
 * Replaces Nether gold ore blocks with a random ore.
 * The chance of a given ore being chosen is determined by the config.
 */
public class OreProcessor extends StructureProcessor {
    public static final OreProcessor INSTANCE = new OreProcessor();
    public static final Codec<OreProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public Structure.StructureBlockInfo process(WorldView worldReader, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Structure.StructureBlockInfo blockInfoLocal, Structure.StructureBlockInfo blockInfoGlobal, StructurePlacementData structurePlacementData) {
        if (blockInfoGlobal.state.getBlock() == Blocks.NETHER_GOLD_ORE) {
            Random random = structurePlacementData.getRandom(blockInfoGlobal.pos);
            // Randomly select ore from list
            BlockState oreBlock = OreChances.get().getRandomOre(random);
            blockInfoGlobal = new Structure.StructureBlockInfo(blockInfoGlobal.pos, oreBlock, blockInfoGlobal.nbt);
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return BSModProcessors.ORE_PROCESSOR;
    }
}
