package com.yungnickyoung.minecraft.betterstrongholds.init;

import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholds;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.*;

public class BSModConfiguredStructures {
    public static ConfiguredStructureFeature<DefaultFeatureConfig, ? extends StructureFeature<DefaultFeatureConfig>> CONFIGURED_BETTER_STRONGHOLD = BSModStructures.BETTER_STRONGHOLD.configure(FeatureConfig.DEFAULT);

    public static void init() {
        registerConfiguredStructures();
        addConfiguredStructuresToBiomes();
    }

    /**
     * Registers the Better Stronghold configured structure feature.
     */
    private static void registerConfiguredStructures() {
        MutableRegistry<ConfiguredStructureFeature<?, ?>> registry = (MutableRegistry<ConfiguredStructureFeature<?, ?>>) BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE;
        Registry.register(registry, new Identifier(BetterStrongholds.MOD_ID, "stronghold"), CONFIGURED_BETTER_STRONGHOLD);
    }

    /**
     * Adds Better Strongholds to valid biomes.
     */
    @SuppressWarnings("deprecation")
    private static void addConfiguredStructuresToBiomes() {
        // Remove vanilla strongholds from all biomes
        BiomeModifications.create(new Identifier(BetterStrongholds.MOD_ID, "vanilla_stronghold_removal"))
            .add(ModificationPhase.REMOVALS,
                biomeSelectionContext -> biomeSelectionContext.hasBuiltInStructure(ConfiguredStructureFeatures.STRONGHOLD),
                modificationContext -> modificationContext.getGenerationSettings().removeStructure(StructureFeature.STRONGHOLD));

        // Add Better Strongholds to all applicable biomes
        BiomeModifications.create(new Identifier(BetterStrongholds.MOD_ID, "better_stronghold_addition"))
            .add(ModificationPhase.ADDITIONS,
                biomeSelectionContext -> !BetterStrongholds.blacklistedBiomes.contains(biomeSelectionContext.getBiomeKey().getValue().toString()),
                modificationContext -> modificationContext.getGenerationSettings().addBuiltInStructure(BSModConfiguredStructures.CONFIGURED_BETTER_STRONGHOLD));
    }
}
