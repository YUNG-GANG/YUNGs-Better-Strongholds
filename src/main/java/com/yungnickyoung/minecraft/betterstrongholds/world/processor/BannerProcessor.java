package com.yungnickyoung.minecraft.betterstrongholds.world.processor;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterstrongholds.init.ModProcessors;
import com.yungnickyoung.minecraft.betterstrongholds.util.BannerFactory;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

@MethodsReturnNonnullByDefault
public class BannerProcessor extends StructureProcessor {
    public static final BannerProcessor INSTANCE = new BannerProcessor();
    public static final Codec<BannerProcessor> CODEC = Codec.unit(() -> INSTANCE);

    // All banners
    public static final BannerFactory.Banner ENDERMAN_BANNER = new BannerFactory.Banner.Builder()
        .blockState(Blocks.MAGENTA_WALL_BANNER.getDefaultState())
        .pattern("ss", 0)
        .pattern("ts", 15)
        .pattern("hhb", 15)
        .pattern("bo", 15)
        .pattern("ms", 15)
        .pattern("cs", 15)
        .build();

    public static final BannerFactory.Banner WITHER_BANNER = new BannerFactory.Banner.Builder()
        .blockState(Blocks.BLACK_WALL_BANNER.getDefaultState())
        .pattern("bs", 7)
        .pattern("cs", 15)
        .pattern("hh", 7)
        .pattern("cre", 15)
        .pattern("sku", 15)
        .build();

    public static final BannerFactory.Banner PORTAL_BANNER = new BannerFactory.Banner.Builder()
        .blockState(Blocks.PURPLE_WALL_BANNER.getDefaultState())
        .pattern("ss", 2)
        .pattern("bri", 10)
        .pattern("cbo", 2)
        .pattern("bo", 15)
        .build();

    public static final List<BannerFactory.Banner> BANNERS = Lists.newArrayList(
        ENDERMAN_BANNER,
        WITHER_BANNER,
        PORTAL_BANNER
    );

    @Override
    public Template.BlockInfo process(IWorldReader worldReader, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Template.BlockInfo blockInfoLocal, Template.BlockInfo blockInfoGlobal, PlacementSettings structurePlacementData, @Nullable Template template) {
        if (blockInfoGlobal.state.getBlock() instanceof AbstractBannerBlock) {
            // Make sure we only operate on the placeholder banners
            if (blockInfoGlobal.state.getBlock() == Blocks.GRAY_WALL_BANNER && (blockInfoGlobal.nbt.get("Patterns") == null || blockInfoGlobal.nbt.getList("Patterns", 10).size() == 0)) {
                BannerFactory.Banner banner = getRandomBanner(structurePlacementData.getRandom(blockInfoGlobal.pos));
                Direction facing = blockInfoGlobal.state.get(BlockStateProperties.HORIZONTAL_FACING);
                BlockState newState = banner.getState().with(BlockStateProperties.HORIZONTAL_FACING, facing);
                CompoundNBT newNBT = copyNBT(banner.getNbt());

                blockInfoGlobal = new Template.BlockInfo(blockInfoGlobal.pos, newState, newNBT);
            }
        }
        return blockInfoGlobal;
    }

    protected IStructureProcessorType<?> getType() {
        return ModProcessors.BANNER_PROCESSOR;
    }

    private BannerFactory.Banner getRandomBanner(Random random) {
        return BANNERS.get(random.nextInt(BANNERS.size()));
    }

    private CompoundNBT copyNBT(CompoundNBT other) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.merge(other);
        return nbt;
    }
}
