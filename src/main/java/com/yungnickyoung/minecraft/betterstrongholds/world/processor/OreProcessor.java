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

public class OreProcessor extends StructureProcessor {
    public static final OreProcessor INSTANCE = new OreProcessor();
    public static final Codec<OreProcessor> CODEC = Codec.unit(() -> INSTANCE);

    private static final Map<Block, Float> oreChances = new LinkedHashMap<>();
    // TODO - provide config options for values
    static {
        oreChances.put(Blocks.COAL_ORE, .2f);
        oreChances.put(Blocks.IRON_ORE, .2f);
        oreChances.put(Blocks.GOLD_ORE, .2f);
        oreChances.put(Blocks.LAPIS_ORE, .15f);
        oreChances.put(Blocks.REDSTONE_ORE, .15f);
        oreChances.put(Blocks.EMERALD_ORE, .05f);
        oreChances.put(Blocks.DIAMOND_ORE, .05f);
    }

    @Nullable
    @Override
    public Template.BlockInfo process(IWorldReader worldReader, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Template.BlockInfo blockInfoLocal, Template.BlockInfo blockInfoGlobal, PlacementSettings structurePlacementData, @Nullable Template template) {
        if (blockInfoGlobal.state.getBlock() == Blocks.NETHER_GOLD_ORE) {
            Random random = structurePlacementData.getRandom(blockInfoGlobal.pos);
            // Randomly select ore from list
            float f = random.nextFloat();
            float currThreshold = 0f;
            for (Map.Entry<Block, Float> entry : oreChances.entrySet()) {
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
        return ModProcessors.ORE_PROCESSOR;
    }
}
