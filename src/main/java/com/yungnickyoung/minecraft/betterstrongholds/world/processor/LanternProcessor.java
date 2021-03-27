package com.yungnickyoung.minecraft.betterstrongholds.world.processor;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterstrongholds.init.ModProcessors;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

/**
 * Replaces some lanterns with air, making the structure look more weathered and less complete.
 */
@MethodsReturnNonnullByDefault
public class LanternProcessor extends StructureProcessor {
    public static final LanternProcessor INSTANCE = new LanternProcessor();
    public static final Codec<LanternProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @ParametersAreNonnullByDefault
    @Override
    public Template.BlockInfo process(IWorldReader worldReader, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Template.BlockInfo blockInfoLocal, Template.BlockInfo blockInfoGlobal, PlacementSettings structurePlacementData, @Nullable Template template) {
        if (blockInfoGlobal.state.isIn(Blocks.LANTERN)) {
            Random random = structurePlacementData.getRandom(blockInfoGlobal.pos);
            float replacementChance = getReplacementChance();
            if (random.nextFloat() > replacementChance)
                blockInfoGlobal = new Template.BlockInfo(blockInfoGlobal.pos, Blocks.AIR.getDefaultState(), blockInfoGlobal.nbt);
        }
        return blockInfoGlobal;
    }

    protected IStructureProcessorType<?> getType() {
        return ModProcessors.LANTERN_PROCESSOR;
    }

    /**
     * Returns lantern replacement chance for the given BlockState.
     *
     * TODO - Use config options instead of hard-coded values.
     */
    private float getReplacementChance() {
        return .2f;
    }
}
