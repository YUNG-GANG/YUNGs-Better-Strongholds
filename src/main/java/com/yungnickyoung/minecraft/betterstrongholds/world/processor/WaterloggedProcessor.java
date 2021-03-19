package com.yungnickyoung.minecraft.betterstrongholds.world.processor;


import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholds;
import com.yungnickyoung.minecraft.betterstrongholds.init.ModProcessors;
import net.minecraft.block.Blocks;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import javax.annotation.Nullable;

public class WaterloggedProcessor extends StructureProcessor {
    public static final WaterloggedProcessor INSTANCE = new WaterloggedProcessor();
    public static final Codec<WaterloggedProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Nullable
    @Override
    public Template.BlockInfo process(IWorldReader worldReader, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Template.BlockInfo blockInfoLocal, Template.BlockInfo blockInfoGlobal, PlacementSettings structurePlacementData, @Nullable Template template) {
        // Workaround for https://bugs.mojang.com/browse/MC-130584
        // Due to a hardcoded field in Templates, any waterloggable blocks in structures replacing water in the world will become waterlogged.
        // Idea of workaround is detect if we are placing a waterloggable block and if so, remove the water in the world instead.
        ChunkPos currentChunk = new ChunkPos(blockInfoGlobal.pos);
        if (blockInfoGlobal.state.getBlock() instanceof IWaterLoggable) {
            if (worldReader.getFluidState(blockInfoGlobal.pos).isTagged(FluidTags.WATER)) {
                blockInfoGlobal = new Template.BlockInfo(blockInfoGlobal.pos, blockInfoGlobal.state.with(BlockStateProperties.WATERLOGGED, false), blockInfoGlobal.nbt);
                worldReader.getChunk(currentChunk.x, currentChunk.z).setBlockState(blockInfoGlobal.pos, Blocks.AIR.getDefaultState(), false);
            }
        }

        // Remove water in adjacent blocks across chunk boundaries as well
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            mutable.setPos(blockInfoGlobal.pos).move(direction);
            if (currentChunk.x != mutable.getX() >> 4 || currentChunk.z != mutable.getZ() >> 4) {
                IChunk sideChunk = worldReader.getChunk(mutable);
                if (sideChunk.getFluidState(mutable).isTagged(FluidTags.WATER)) {
                    sideChunk.setBlockState(mutable, Blocks.AIR.getDefaultState(), false);
                }
            }
        }

        return blockInfoGlobal;
    }

    protected IStructureProcessorType<?> getType() {
        return ModProcessors.WATERLOGGED_PROCESSOR;
    }
}

