package com.yungnickyoung.minecraft.betterstrongholds.services;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class FabricRegistryHelper implements IRegistryHelper {
    @Override
    public void registerStructureProcessorType(ResourceLocation resourceLocation, StructureProcessorType<? extends StructureProcessor> structureProcessorType) {
        Registry.register(Registry.STRUCTURE_PROCESSOR, resourceLocation, structureProcessorType);
    }

    @Override
    public void registerStructurePlacementType(ResourceLocation resourceLocation, StructurePlacementType<? extends StructurePlacement> structurePlacementType) {
        Registry.register(Registry.STRUCTURE_PLACEMENT_TYPE, resourceLocation, structurePlacementType);
    }
}
