package com.yungnickyoung.minecraft.betterstrongholds.world.processor;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholds;
import com.yungnickyoung.minecraft.betterstrongholds.init.ModProcessors;
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

@MethodsReturnNonnullByDefault
public class AirProcessor extends StructureProcessor {
    public static final AirProcessor INSTANCE = new AirProcessor();
    public static final Codec<AirProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Nullable
    @Override
    public Template.BlockInfo process(IWorldReader worldReader, BlockPos pos, BlockPos blockPos, Template.BlockInfo structureBlockInfoLocal, Template.BlockInfo structureBlockInfoWorld, PlacementSettings structurePlacementData, @Nullable Template template) {
        if (structureBlockInfoWorld.state.isIn(Blocks.CYAN_CONCRETE)) {
            if (worldReader.getBlockState(structureBlockInfoWorld.pos).getMaterial() == Material.AIR) {
                worldReader.getChunk(structureBlockInfoWorld.pos).setBlockState(structureBlockInfoWorld.pos, Blocks.AIR.getDefaultState(), false);
                BetterStrongholds.LOGGER.info("AIR~~!~!");
            } else {
                worldReader.getChunk(structureBlockInfoWorld.pos).setBlockState(structureBlockInfoWorld.pos, Blocks.STONE_BRICKS.getDefaultState(), false);
                BetterStrongholds.LOGGER.info("BRICKS~~!~!");
            }
        }
        return structureBlockInfoWorld;
    }

    protected IStructureProcessorType<?> getType() {
        return ModProcessors.AIR_PROCESSORS;
    }
}
