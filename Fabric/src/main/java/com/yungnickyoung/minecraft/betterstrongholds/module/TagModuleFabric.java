package com.yungnickyoung.minecraft.betterstrongholds.module;

import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholdsCommon;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public class TagModuleFabric {
    public static void init() {
        TagModule.HAS_BETTER_STRONGHOLD = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(BetterStrongholdsCommon.MOD_ID, "has_better_stronghold"));
    }
}
