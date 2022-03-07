package com.yungnickyoung.minecraft.betterstrongholds.init;

import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholds;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class BSModTags {
    public static TagKey<Biome> HAS_BETTER_STRONGHOLD;

    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(BSModTags::commonSetup);
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            HAS_BETTER_STRONGHOLD = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(BetterStrongholds.MOD_ID, "has_better_stronghold"));
        });
    }
}
