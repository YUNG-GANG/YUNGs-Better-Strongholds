package com.yungnickyoung.minecraft.betterstrongholds.module;

import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholdsCommon;
import com.yungnickyoung.minecraft.betterstrongholds.services.Services;
import com.yungnickyoung.minecraft.betterstrongholds.world.placement.BetterStrongholdsPlacement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;

public class StructurePlacementTypeModule {
    public static final StructurePlacementType<BetterStrongholdsPlacement> BETTER_STRONGHOLD_PLACEMENT = () -> BetterStrongholdsPlacement.CODEC;

    public static void init() {
        Services.REGISTRY.registerStructurePlacementType(new ResourceLocation(BetterStrongholdsCommon.MOD_ID, "stronghold"), BETTER_STRONGHOLD_PLACEMENT);
    }
}
