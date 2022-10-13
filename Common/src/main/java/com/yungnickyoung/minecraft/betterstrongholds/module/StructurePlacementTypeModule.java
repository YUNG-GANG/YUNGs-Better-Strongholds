package com.yungnickyoung.minecraft.betterstrongholds.module;

import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholdsCommon;
import com.yungnickyoung.minecraft.betterstrongholds.world.placement.BetterStrongholdsPlacement;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;

@AutoRegister(BetterStrongholdsCommon.MOD_ID)
public class StructurePlacementTypeModule {
    @AutoRegister("stronghold")
    public static final StructurePlacementType<BetterStrongholdsPlacement> BETTER_STRONGHOLD_PLACEMENT = () -> BetterStrongholdsPlacement.CODEC;
}
