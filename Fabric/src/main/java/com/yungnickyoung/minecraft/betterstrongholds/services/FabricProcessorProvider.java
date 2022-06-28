package com.yungnickyoung.minecraft.betterstrongholds.services;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterstrongholds.world.processor.ArmorStandProcessor;
import com.yungnickyoung.minecraft.betterstrongholds.world.processor.ItemFrameProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;

public class FabricProcessorProvider implements IProcessorProvider {
    @Override
    public Codec<StructureProcessor> armorStandProcessorCodec() {
        return ArmorStandProcessor.CODEC;
    }

    @Override
    public Codec<StructureProcessor> itemFrameProcessorCodec() {
        return ItemFrameProcessor.CODEC;
    }
}
