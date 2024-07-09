package com.yungnickyoung.minecraft.betterstrongholds.world.processor;

import com.google.common.collect.Lists;
import com.mojang.serialization.MapCodec;
import com.yungnickyoung.minecraft.betterstrongholds.module.StructureProcessorTypeModule;
import com.yungnickyoung.minecraft.yungsapi.world.banner.Banner;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AbstractBannerBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BannerPatterns;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.List;

/**
 * Replaces gray wall banners with a random banner from a pool of banners.
 */
public class BannerProcessor extends StructureProcessor {
    public static final BannerProcessor INSTANCE = new BannerProcessor();
    public static final MapCodec<BannerProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    // All banners
    public static final Banner ENDERMAN_WALL_BANNER = new Banner.Builder()
        .blockState(Blocks.MAGENTA_WALL_BANNER.defaultBlockState())
        .pattern(BannerPatterns.STRIPE_SMALL, DyeColor.WHITE)
        .pattern(BannerPatterns.STRIPE_TOP, DyeColor.BLACK)
        .pattern(BannerPatterns.HALF_HORIZONTAL_MIRROR, DyeColor.BLACK)
        .pattern(BannerPatterns.BORDER, DyeColor.BLACK)
        .pattern(BannerPatterns.STRIPE_MIDDLE, DyeColor.BLACK)
        .pattern(BannerPatterns.STRIPE_CENTER, DyeColor.BLACK)
        .build();

    public static final Banner WITHER_WALL_BANNER = new Banner.Builder()
        .blockState(Blocks.BLACK_WALL_BANNER.defaultBlockState())
        .pattern(BannerPatterns.STRIPE_BOTTOM, DyeColor.GRAY)
        .pattern(BannerPatterns.STRIPE_CENTER, DyeColor.BLACK)
        .pattern(BannerPatterns.HALF_HORIZONTAL, DyeColor.GRAY)
        .pattern(BannerPatterns.CREEPER, DyeColor.BLACK)
        .pattern(BannerPatterns.SKULL, DyeColor.BLACK)
        .build();

    public static final Banner PORTAL_WALL_BANNER = new Banner.Builder()
        .blockState(Blocks.PURPLE_WALL_BANNER.defaultBlockState())
        .pattern(BannerPatterns.STRIPE_SMALL, DyeColor.MAGENTA)
        .pattern(BannerPatterns.BRICKS, DyeColor.PURPLE)
        .pattern(BannerPatterns.CURLY_BORDER, DyeColor.MAGENTA)
        .pattern(BannerPatterns.BORDER, DyeColor.BLACK)
        .build();

    public static final List<Banner> WALL_BANNERS = Lists.newArrayList(
        ENDERMAN_WALL_BANNER,
        WITHER_WALL_BANNER,
        PORTAL_WALL_BANNER
    );

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                             StructureTemplate.StructureBlockInfo blockInfoLocal,
                                                             StructureTemplate.StructureBlockInfo blockInfoGlobal,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfoGlobal.state().getBlock() instanceof AbstractBannerBlock) {
            // Make sure we only operate on the placeholder banners
            if (blockInfoGlobal.state().getBlock() == Blocks.GRAY_WALL_BANNER && (blockInfoGlobal.nbt().get("patterns") == null || blockInfoGlobal.nbt().getList("patterns", 10).isEmpty())) {
                Banner banner = getRandomBanner(structurePlacementData.getRandom(blockInfoGlobal.pos()));
                Direction facing = blockInfoGlobal.state().getValue(BlockStateProperties.HORIZONTAL_FACING);
                BlockState newState = banner.getState().setValue(BlockStateProperties.HORIZONTAL_FACING, facing);
                CompoundTag newNBT = copyNBT(banner.getNbt());

                blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos(), newState, newNBT);
            }
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorTypeModule.BANNER_PROCESSOR;
    }

    private Banner getRandomBanner(RandomSource randomSource) {
        return WALL_BANNERS.get(randomSource.nextInt(WALL_BANNERS.size()));
    }

    private CompoundTag copyNBT(CompoundTag other) {
        CompoundTag nbt = new CompoundTag();
        nbt.merge(other);
        return nbt;
    }
}
