package com.yungnickyoung.minecraft.betterstrongholds.services;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;

public interface IProcessorProvider {
    MapCodec<StructureProcessor> armorStandProcessorCodec();

    MapCodec<StructureProcessor> itemFrameProcessorCodec();
}
