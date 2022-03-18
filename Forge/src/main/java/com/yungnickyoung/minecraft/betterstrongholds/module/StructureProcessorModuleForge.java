package com.yungnickyoung.minecraft.betterstrongholds.module;

import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholdsCommon;
import com.yungnickyoung.minecraft.betterstrongholds.world.processor.ArmorStandProcessor;
import com.yungnickyoung.minecraft.betterstrongholds.world.processor.ItemFrameProcessor;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class StructureProcessorModuleForge {
    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(StructureProcessorModuleForge::commonSetup);
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            register("air_processor", StructureProcessorModule.AIR_PROCESSOR);
            register("cobweb_processor", StructureProcessorModule.COBWEB_PROCESSOR);
            register("torch_processor", StructureProcessorModule.TORCH_PROCESSOR);
            register("lantern_processor", StructureProcessorModule.LANTERN_PROCESSOR);
            register("banner_processor", StructureProcessorModule.BANNER_PROCESSOR);
            register("waterlogged_processor", StructureProcessorModule.WATERLOGGED_PROCESSOR);
            register("ore_processor", StructureProcessorModule.ORE_PROCESSOR);
            register("rare_block_processor", StructureProcessorModule.RARE_BLOCK_PROCESSOR);
            register("redstone_processor", StructureProcessorModule.REDSTONE_PROCESSOR);
            register("leg_processor", StructureProcessorModule.LEG_PROCESSOR);
            StructureProcessorModule.ARMORSTAND_PROCESSOR = register("armorstand_processor", () -> ArmorStandProcessor.CODEC);
            StructureProcessorModule.ITEMFRAME_PROCESSOR = register("itemframe_processor", () -> ItemFrameProcessor.CODEC);
        });
    }

    private static <P extends StructureProcessor> StructureProcessorType<P> register(String name, StructureProcessorType<P> processorType) {
        return Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterStrongholdsCommon.MOD_ID, name), processorType);
    }
}
