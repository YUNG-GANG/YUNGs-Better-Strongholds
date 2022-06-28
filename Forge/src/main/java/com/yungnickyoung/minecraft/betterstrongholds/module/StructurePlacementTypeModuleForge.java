package com.yungnickyoung.minecraft.betterstrongholds.module;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.HashMap;
import java.util.Map;

public class StructurePlacementTypeModuleForge {
    public static Map<ResourceLocation, StructurePlacementType<? extends StructurePlacement>> STRUCTURE_PLACEMENT_TYPES = new HashMap<>();

    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(StructurePlacementTypeModuleForge::commonSetup);
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> STRUCTURE_PLACEMENT_TYPES.forEach((name, structurePieceType) -> Registry.register(Registry.STRUCTURE_PLACEMENT_TYPE, name, structurePieceType)));
    }
}
