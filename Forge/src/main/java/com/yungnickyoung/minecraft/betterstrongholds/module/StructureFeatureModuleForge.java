package com.yungnickyoung.minecraft.betterstrongholds.module;

import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholdsCommon;
import com.yungnickyoung.minecraft.betterstrongholds.world.BetterStrongholdStructureFeature;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;

public class StructureFeatureModuleForge {
    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(StructureFeature.class, StructureFeatureModuleForge::registerStructures);
    }

    private static void registerStructures(RegistryEvent.Register<StructureFeature<?>> event) {
        StructureFeatureModule.BETTER_STRONGHOLD = register(event.getRegistry(), "stronghold", new BetterStrongholdStructureFeature());
    }

    private static <FC extends FeatureConfiguration> StructureFeature<FC> register(IForgeRegistry<StructureFeature<?>> registry, String name, StructureFeature<FC> structureFeature) {
        structureFeature.setRegistryName(new ResourceLocation(BetterStrongholdsCommon.MOD_ID, name));
        registry.register(structureFeature);
        return structureFeature;
    }
}
