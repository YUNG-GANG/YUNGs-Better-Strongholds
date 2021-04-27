package com.yungnickyoung.minecraft.betterstrongholds.world.processor;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterstrongholds.init.BSModProcessors;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Ensures redstone doesn't spawn floating in the air.
 */
@MethodsReturnNonnullByDefault
public class RedstoneProcessor extends StructureProcessor {
    public static final RedstoneProcessor INSTANCE = new RedstoneProcessor();
    public static final Codec<RedstoneProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @ParametersAreNonnullByDefault
    @Override
    public Template.BlockInfo process(IWorldReader worldReader, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Template.BlockInfo blockInfoLocal, Template.BlockInfo blockInfoGlobal, PlacementSettings structurePlacementData, @Nullable Template template) {
        if (blockInfoGlobal.state.isIn(Blocks.REDSTONE_WIRE)) {
            if (!worldReader.getBlockState(blockInfoGlobal.pos.down()).isSolidSide(worldReader, blockInfoGlobal.pos.down(), Direction.UP)) {
                worldReader.getChunk(blockInfoGlobal.pos).setBlockState(blockInfoGlobal.pos.down(), Blocks.STONE_BRICKS.getDefaultState(), false);
            }
        }
        return blockInfoGlobal;
    }

    protected IStructureProcessorType<?> getType() {
        return BSModProcessors.REDSTONE_PROCESSOR;
    }
}
