package com.yungnickyoung.minecraft.betterstrongholds.services;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public interface IRegistryHelper {
    void registerStructureProcessorType(ResourceLocation resourceLocation, StructureProcessorType<? extends StructureProcessor> structureProcessorType);

    void registerStructurePlacementType(ResourceLocation resourceLocation, StructurePlacementType<? extends StructurePlacement> structurePlacementType);
}
