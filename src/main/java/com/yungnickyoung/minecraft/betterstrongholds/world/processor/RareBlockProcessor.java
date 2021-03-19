package com.yungnickyoung.minecraft.betterstrongholds.world.processor;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterstrongholds.init.ModProcessors;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class RareBlockProcessor extends StructureProcessor {
    public static final RareBlockProcessor INSTANCE = new RareBlockProcessor();
    public static final Codec<RareBlockProcessor> CODEC = Codec.unit(() -> INSTANCE);

    private static final Map<Block, Float> blockChances = new LinkedHashMap<>();
    // TODO - provide config options for values
    static {
        blockChances.put(Blocks.IRON_BLOCK, .3f);
        blockChances.put(Blocks.QUARTZ_BLOCK, .3f);
        blockChances.put(Blocks.GOLD_BLOCK, .3f);
        blockChances.put(Blocks.DIAMOND_BLOCK, .1f);
    }

    @Nullable
    @Override
    public Template.BlockInfo process(IWorldReader worldReader, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Template.BlockInfo blockInfoLocal, Template.BlockInfo blockInfoGlobal, PlacementSettings structurePlacementData, @Nullable Template template) {
        if (blockInfoGlobal.state.getBlock() == Blocks.PURPUR_BLOCK) {
            Random random = structurePlacementData.getRandom(blockInfoGlobal.pos);
            // Randomly select ore from list
            float f = random.nextFloat();
            float currThreshold = 0f;
            for (Map.Entry<Block, Float> entry : blockChances.entrySet()) {
                Block block = entry.getKey();
                float chance = entry.getValue();
                currThreshold += chance;
                if (f < currThreshold) {
                    blockInfoGlobal = new Template.BlockInfo(blockInfoGlobal.pos, block.getDefaultState(), blockInfoGlobal.nbt);
                    break;
                }
            }
        }
        return blockInfoGlobal;
    }

    protected IStructureProcessorType<?> getType() {
        return ModProcessors.RARE_BLOCK_PROCESSOR;
    }
}

