package com.yungnickyoung.minecraft.betterstrongholds.world.processor;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterstrongholds.init.BSModProcessors;
import com.yungnickyoung.minecraft.betterstrongholds.world.RareBlockChances;
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
 * Replaces purpur blocks with a random rare block
 * The chance of a given block being chosen is determined by the config.
 */
public class RareBlockProcessor extends StructureProcessor {
    public static final RareBlockProcessor INSTANCE = new RareBlockProcessor();
    public static final Codec<RareBlockProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public Structure.StructureBlockInfo process(WorldView worldReader, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Structure.StructureBlockInfo blockInfoLocal, Structure.StructureBlockInfo blockInfoGlobal, StructurePlacementData structurePlacementData) {
        if (blockInfoGlobal.state.getBlock() == Blocks.PURPUR_BLOCK) {
            Random random = structurePlacementData.getRandom(blockInfoGlobal.pos);
            // Randomly select ore from list
            BlockState rareBlock = RareBlockChances.get().getRandomRareBlock(random);
            blockInfoGlobal = new Structure.StructureBlockInfo(blockInfoGlobal.pos, rareBlock, blockInfoGlobal.tag);

        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return BSModProcessors.RARE_BLOCK_PROCESSOR;
    }
}

