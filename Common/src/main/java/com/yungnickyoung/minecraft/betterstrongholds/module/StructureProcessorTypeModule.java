package com.yungnickyoung.minecraft.betterstrongholds.module;

import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholdsCommon;
import com.yungnickyoung.minecraft.betterstrongholds.services.Services;
import com.yungnickyoung.minecraft.betterstrongholds.world.processor.*;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

@AutoRegister(BetterStrongholdsCommon.MOD_ID)
public class StructureProcessorTypeModule {
    @AutoRegister("ruin_processor")
    public static StructureProcessorType<RuinProcessor> RUIN_PROCESSOR = () -> RuinProcessor.CODEC;

    @AutoRegister("banner_processor")
    public static StructureProcessorType<BannerProcessor> BANNER_PROCESSOR = () -> BannerProcessor.CODEC;

    @AutoRegister("ore_processor")
    public static StructureProcessorType<OreProcessor> ORE_PROCESSOR = () -> OreProcessor.CODEC;

    @AutoRegister("rare_block_processor")
    public static StructureProcessorType<RareBlockProcessor> RARE_BLOCK_PROCESSOR = () -> RareBlockProcessor.CODEC;

    @AutoRegister("redstone_processor")
    public static StructureProcessorType<RedstoneProcessor> REDSTONE_PROCESSOR = () -> RedstoneProcessor.CODEC;

    @AutoRegister("leg_processor")
    public static StructureProcessorType<LegProcessor> LEG_PROCESSOR = () -> LegProcessor.CODEC;

    @AutoRegister("end_portal_frame_processor")
    public static StructureProcessorType<EndPortalFrameProcessor> END_PORTAL_FRAME_PROCESSOR = () -> EndPortalFrameProcessor.CODEC;

    @AutoRegister("armorstand_processor")
    public static StructureProcessorType<StructureProcessor> ARMORSTAND_PROCESSOR = () -> Services.PROCESSORS.armorStandProcessorCodec();

    @AutoRegister("itemframe_processor")
    public static StructureProcessorType<StructureProcessor> ITEMFRAME_PROCESSOR = () -> Services.PROCESSORS.itemFrameProcessorCodec();
}
