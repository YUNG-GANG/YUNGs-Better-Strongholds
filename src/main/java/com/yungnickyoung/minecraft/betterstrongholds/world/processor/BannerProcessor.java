package com.yungnickyoung.minecraft.betterstrongholds.world.processor;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterstrongholds.init.BSModProcessors;
import com.yungnickyoung.minecraft.betterstrongholds.util.Banner;
import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Properties;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldView;

import java.util.List;
import java.util.Random;

/**
 * Replaces gray wall banners with a random banner from a pool of banners.
 */
public class BannerProcessor extends StructureProcessor {
    public static final BannerProcessor INSTANCE = new BannerProcessor();
    public static final Codec<BannerProcessor> CODEC = Codec.unit(() -> INSTANCE);

    // All banners
    public static final Banner ENDERMAN_WALL_BANNER = new Banner.Builder()
        .blockState(Blocks.MAGENTA_WALL_BANNER.getDefaultState())
        .pattern("ss", 0)
        .pattern("ts", 15)
        .pattern("hhb", 15)
        .pattern("bo", 15)
        .pattern("ms", 15)
        .pattern("cs", 15)
        .build();

    public static final Banner WITHER_WALL_BANNER = new Banner.Builder()
        .blockState(Blocks.BLACK_WALL_BANNER.getDefaultState())
        .pattern("bs", 7)
        .pattern("cs", 15)
        .pattern("hh", 7)
        .pattern("cre", 15)
        .pattern("sku", 15)
        .build();

    public static final Banner PORTAL_WALL_BANNER = new Banner.Builder()
        .blockState(Blocks.PURPLE_WALL_BANNER.getDefaultState())
        .pattern("ss", 2)
        .pattern("bri", 10)
        .pattern("cbo", 2)
        .pattern("bo", 15)
        .build();

    public static final List<Banner> WALL_BANNERS = Lists.newArrayList(
        ENDERMAN_WALL_BANNER,
        WITHER_WALL_BANNER,
        PORTAL_WALL_BANNER
    );

    @Override
    public Structure.StructureBlockInfo process(WorldView worldReader, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Structure.StructureBlockInfo blockInfoLocal, Structure.StructureBlockInfo blockInfoGlobal, StructurePlacementData structurePlacementData) {
        if (blockInfoGlobal.state.getBlock() instanceof AbstractBannerBlock) {
            // Make sure we only operate on the placeholder banners
            if (blockInfoGlobal.state.getBlock() == Blocks.GRAY_WALL_BANNER && (blockInfoGlobal.nbt.get("Patterns") == null || blockInfoGlobal.nbt.getList("Patterns", 10).size() == 0)) {
                Banner banner = getRandomBanner(structurePlacementData.getRandom(blockInfoGlobal.pos));
                Direction facing = blockInfoGlobal.state.get(Properties.HORIZONTAL_FACING);
                BlockState newState = banner.getState().with(Properties.HORIZONTAL_FACING, facing);
                NbtCompound newNBT = copyNBT(banner.getNbt());

                blockInfoGlobal = new Structure.StructureBlockInfo(blockInfoGlobal.pos, newState, newNBT);
            }
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return BSModProcessors.BANNER_PROCESSOR;
    }

    private Banner getRandomBanner(Random random) {
        return WALL_BANNERS.get(random.nextInt(WALL_BANNERS.size()));
    }

    private NbtCompound copyNBT(NbtCompound other) {
        NbtCompound nbt = new NbtCompound();
        nbt.copyFrom(other);
        return nbt;
    }
}
