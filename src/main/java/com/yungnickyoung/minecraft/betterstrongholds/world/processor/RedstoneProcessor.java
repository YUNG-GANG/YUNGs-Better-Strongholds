package com.yungnickyoung.minecraft.betterstrongholds.world.processor;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterstrongholds.init.BSModProcessors;
import com.yungnickyoung.minecraft.yungsapi.world.processor.ISafeWorldModifier;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldView;

import java.util.Optional;

/**
 * Ensures redstone doesn't spawn floating in the air.
 */
public class RedstoneProcessor extends StructureProcessor implements ISafeWorldModifier {
    public static final RedstoneProcessor INSTANCE = new RedstoneProcessor();
    public static final Codec<RedstoneProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public Structure.StructureBlockInfo process(WorldView worldReader, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Structure.StructureBlockInfo blockInfoLocal, Structure.StructureBlockInfo blockInfoGlobal, StructurePlacementData structurePlacementData) {
        if (blockInfoGlobal.state.isOf(Blocks.REDSTONE_WIRE)) {
            Optional<BlockState> belowBlockState = getBlockStateSafe(worldReader, blockInfoGlobal.pos.down());
            if (belowBlockState.isEmpty() || belowBlockState.get().isSideSolidFullSquare(worldReader, blockInfoGlobal.pos.down(), Direction.UP)) {
                setBlockStateSafe(worldReader, blockInfoGlobal.pos.down(), Blocks.STONE_BRICKS.getDefaultState());
            }
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return BSModProcessors.REDSTONE_PROCESSOR;
    }
}
