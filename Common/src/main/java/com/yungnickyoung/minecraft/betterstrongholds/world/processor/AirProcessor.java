package com.yungnickyoung.minecraft.betterstrongholds.world.processor;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterstrongholds.module.StructureProcessorModule;
import com.yungnickyoung.minecraft.yungsapi.world.processor.ISafeWorldModifier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

/**
 * Processor for replacing cyan concrete with air or stone bricks.
 * Intended to give walls and other pieces a ruined appearance that blends in with caves.
 */
public class AirProcessor extends StructureProcessor implements ISafeWorldModifier {
    public static final AirProcessor INSTANCE = new AirProcessor();
    public static final Codec<AirProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                BlockPos jigsawPiecePos,
                                                BlockPos jigsawPieceBottomCenterPos,
                                                StructureTemplate.StructureBlockInfo blockInfoLocal,
                                                StructureTemplate.StructureBlockInfo blockInfoGlobal,
                                                StructurePlaceSettings structurePlacementData) {
        if (blockInfoGlobal.state.is(Blocks.CYAN_CONCRETE)) {
            if (isBlockStateAirSafe(levelReader, blockInfoGlobal.pos)) {
                blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos, Blocks.AIR.defaultBlockState(), blockInfoGlobal.nbt);
            } else {
                blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos, Blocks.STONE_BRICKS.defaultBlockState(), blockInfoGlobal.nbt);
            }
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorModule.AIR_PROCESSOR;
    }
}
