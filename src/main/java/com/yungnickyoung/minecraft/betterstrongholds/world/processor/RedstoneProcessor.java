package com.yungnickyoung.minecraft.betterstrongholds.world.processor;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterstrongholds.init.BSModProcessors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.Optional;

/**
 * Ensures redstone doesn't spawn floating in the air.
 */
public class RedstoneProcessor extends StructureProcessor implements ISafeWorldModifier {
    public static final RedstoneProcessor INSTANCE = new RedstoneProcessor();
    public static final Codec<RedstoneProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                             StructureTemplate.StructureBlockInfo blockInfoLocal,
                                                             StructureTemplate.StructureBlockInfo blockInfoGlobal,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfoGlobal.state.is(Blocks.REDSTONE_WIRE)) {
            Optional<BlockState> belowBlockState = getBlockStateSafe(levelReader, blockInfoGlobal.pos.below());
            if (belowBlockState.isEmpty() || belowBlockState.get().isFaceSturdy(levelReader, blockInfoGlobal.pos.below(), Direction.UP)) {
                setBlockStateSafe(levelReader, blockInfoGlobal.pos.below(), Blocks.STONE_BRICKS.defaultBlockState());
            }
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return BSModProcessors.REDSTONE_PROCESSOR;
    }
}
