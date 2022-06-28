package com.yungnickyoung.minecraft.betterstrongholds.module;

import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholdsCommon;
import com.yungnickyoung.minecraft.betterstrongholds.services.Services;
import com.yungnickyoung.minecraft.betterstrongholds.world.processor.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class StructureProcessorTypeModule {
    public static StructureProcessorType<AirProcessor> AIR_PROCESSOR = () -> AirProcessor.CODEC;
    public static StructureProcessorType<CobwebProcessor> COBWEB_PROCESSOR = () -> CobwebProcessor.CODEC;
    public static StructureProcessorType<TorchProcessor> TORCH_PROCESSOR = () -> TorchProcessor.CODEC;
    public static StructureProcessorType<LanternProcessor> LANTERN_PROCESSOR = () -> LanternProcessor.CODEC;
    public static StructureProcessorType<BannerProcessor> BANNER_PROCESSOR = () -> BannerProcessor.CODEC;
    public static StructureProcessorType<WaterloggedProcessor> WATERLOGGED_PROCESSOR = () -> WaterloggedProcessor.CODEC;
    public static StructureProcessorType<OreProcessor> ORE_PROCESSOR = () -> OreProcessor.CODEC;
    public static StructureProcessorType<RareBlockProcessor> RARE_BLOCK_PROCESSOR = () -> RareBlockProcessor.CODEC;
    public static StructureProcessorType<RedstoneProcessor> REDSTONE_PROCESSOR = () -> RedstoneProcessor.CODEC;
    public static StructureProcessorType<LegProcessor> LEG_PROCESSOR = () -> LegProcessor.CODEC;
    public static StructureProcessorType<StructureProcessor> ARMORSTAND_PROCESSOR = () -> Services.PROCESSORS.armorStandProcessorCodec();
    public static StructureProcessorType<StructureProcessor> ITEMFRAME_PROCESSOR = () -> Services.PROCESSORS.itemFrameProcessorCodec();

    public static void init() {
        register("air_processor", AIR_PROCESSOR);
        register("cobweb_processor", COBWEB_PROCESSOR);
        register("torch_processor", TORCH_PROCESSOR);
        register("lantern_processor", LANTERN_PROCESSOR);
        register("banner_processor", BANNER_PROCESSOR);
        register("waterlogged_processor", WATERLOGGED_PROCESSOR);
        register("ore_processor", ORE_PROCESSOR);
        register("rare_block_processor", RARE_BLOCK_PROCESSOR);
        register("redstone_processor", REDSTONE_PROCESSOR);
        register("leg_processor", LEG_PROCESSOR);
        register("armorstand_processor", ARMORSTAND_PROCESSOR);
        register("itemframe_processor", ITEMFRAME_PROCESSOR);
    }

    private static void register(String name, StructureProcessorType<?> processorType) {
        Services.REGISTRY.registerStructureProcessorType(new ResourceLocation(BetterStrongholdsCommon.MOD_ID, name), processorType);
    }
}
