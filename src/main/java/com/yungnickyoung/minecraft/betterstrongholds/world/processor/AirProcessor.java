package com.yungnickyoung.minecraft.betterstrongholds.world.processor;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterstrongholds.init.BSModProcessors;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Processor for replacing cyan concrete with air or stone bricks.
 * Intended to give walls and other pieces a ruined appearance that blends in with caves.
 */
@MethodsReturnNonnullByDefault
public class AirProcessor extends StructureProcessor {
    public static final AirProcessor INSTANCE = new AirProcessor();
    public static final Codec<AirProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @ParametersAreNonnullByDefault
    @Override
    public Template.BlockInfo process(IWorldReader worldReader, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Template.BlockInfo blockInfoLocal, Template.BlockInfo blockInfoGlobal, PlacementSettings structurePlacementData, @Nullable Template template) {
        if (blockInfoGlobal.state.isIn(Blocks.CYAN_CONCRETE)) {
            if (worldReader.getBlockState(blockInfoGlobal.pos).getMaterial() == Material.AIR) {
                blockInfoGlobal = new Template.BlockInfo(blockInfoGlobal.pos, Blocks.AIR.getDefaultState(), blockInfoGlobal.nbt);
            } else {
                blockInfoGlobal = new Template.BlockInfo(blockInfoGlobal.pos, Blocks.STONE_BRICKS.getDefaultState(), blockInfoGlobal.nbt);
            }
        }
        return blockInfoGlobal;
    }

    protected IStructureProcessorType<?> getType() {
        return BSModProcessors.AIR_PROCESSOR;
    }
}
