package com.yungnickyoung.minecraft.betterstrongholds.world.processor;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholds;
import com.yungnickyoung.minecraft.betterstrongholds.init.ModProcessors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

public class BannerEntityProcessor extends StructureProcessor {
    public static final BannerEntityProcessor INSTANCE = new BannerEntityProcessor();
    public static final Codec<BannerEntityProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public Template.EntityInfo processEntity(IWorldReader world, BlockPos seedPos, Template.EntityInfo rawEntityInfo, Template.EntityInfo entityInfo, PlacementSettings placementSettings, Template template) {
        BetterStrongholds.LOGGER.info(entityInfo);
        return entityInfo;
    }

    protected IStructureProcessorType<?> getType() {
        return ModProcessors.BANNER_PROCESSOR;
    }
}
