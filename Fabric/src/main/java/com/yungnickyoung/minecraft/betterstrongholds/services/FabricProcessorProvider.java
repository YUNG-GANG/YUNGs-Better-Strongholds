package com.yungnickyoung.minecraft.betterstrongholds.services;

import com.mojang.serialization.MapCodec;
import com.yungnickyoung.minecraft.betterstrongholds.world.processor.ArmorStandProcessor;
import com.yungnickyoung.minecraft.betterstrongholds.world.processor.ItemFrameProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;

public class FabricProcessorProvider implements IProcessorProvider {
    @Override
    public MapCodec<StructureProcessor> armorStandProcessorCodec() {
        return ArmorStandProcessor.CODEC;
    }

    @Override
    public MapCodec<StructureProcessor> itemFrameProcessorCodec() {
        return ItemFrameProcessor.CODEC;
    }
}
