package com.yungnickyoung.minecraft.betterstrongholds.module;

import com.mojang.serialization.MapCodec;
import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholdsCommon;
import com.yungnickyoung.minecraft.betterstrongholds.services.Services;
import com.yungnickyoung.minecraft.betterstrongholds.world.processor.*;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;

@AutoRegister(BetterStrongholdsCommon.MOD_ID)
public class StructureProcessorTypeModule {
    @AutoRegister("ruin_processor")
    public static MapCodec<? extends StructureProcessor> RUIN_PROCESSOR = RuinProcessor.CODEC;

    @AutoRegister("banner_processor")
    public static MapCodec<? extends StructureProcessor> BANNER_PROCESSOR = BannerProcessor.CODEC;

    @AutoRegister("ore_processor")
    public static MapCodec<? extends StructureProcessor> ORE_PROCESSOR = OreProcessor.CODEC;

    @AutoRegister("rare_block_processor")
    public static MapCodec<? extends StructureProcessor> RARE_BLOCK_PROCESSOR = RareBlockProcessor.CODEC;

    @AutoRegister("redstone_processor")
    public static MapCodec<? extends StructureProcessor> REDSTONE_PROCESSOR = RedstoneProcessor.CODEC;

    @AutoRegister("leg_processor")
    public static MapCodec<? extends StructureProcessor> LEG_PROCESSOR = LegProcessor.CODEC;

    @AutoRegister("end_portal_frame_processor")
    public static MapCodec<? extends StructureProcessor> END_PORTAL_FRAME_PROCESSOR = EndPortalFrameProcessor.CODEC;

    @AutoRegister("armorstand_processor")
    public static MapCodec<? extends StructureProcessor> ARMORSTAND_PROCESSOR = Services.PROCESSORS.armorStandProcessorCodec();

    @AutoRegister("itemframe_processor")
    public static MapCodec<? extends StructureProcessor> ITEMFRAME_PROCESSOR = Services.PROCESSORS.itemFrameProcessorCodec();
}
