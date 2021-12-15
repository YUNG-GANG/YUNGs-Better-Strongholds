package com.yungnickyoung.minecraft.betterstrongholds.init;

import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholds;
import com.yungnickyoung.minecraft.betterstrongholds.mixin.accessor.StructureSettingsAccessor;
import com.yungnickyoung.minecraft.betterstrongholds.world.jigsaw.BetterStrongholdsFeatureConfiguration;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.StructureFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;

import java.util.HashMap;
import java.util.Map;

public class BSModConfiguredStructures {
    public static ConfiguredStructureFeature<?, ?> CONFIGURED_BETTER_STRONGHOLD = BSModStructureFeatures.BETTER_STRONGHOLD
            .configured(new BetterStrongholdsFeatureConfiguration(
                    new ResourceLocation(BetterStrongholds.MOD_ID, "starts"),
                    BetterStrongholds.CONFIG.betterStrongholds.general.strongholdSize));

    public static void init() {
        registerConfiguredStructures();
        addConfiguredStructuresToBiomes();
        enforceDimensionWhitelist();
    }

    /**
     * Registers the Better Stronghold configured structure feature.
     */
    private static void registerConfiguredStructures() {
        Registry<ConfiguredStructureFeature<?, ?>> registry = BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE;
        Registry.register(registry, new ResourceLocation(BetterStrongholds.MOD_ID, "stronghold"), CONFIGURED_BETTER_STRONGHOLD);
    }

    /**
     * Adds Better Strongholds to valid biomes.
     */
    private static void addConfiguredStructuresToBiomes() {
        // Remove vanilla strongholds from all biomes
        BiomeModifications.create(new ResourceLocation(BetterStrongholds.MOD_ID, "vanilla_stronghold_removal"))
            .add(ModificationPhase.REMOVALS,
                biomeSelectionContext -> biomeSelectionContext.hasBuiltInStructure(StructureFeatures.STRONGHOLD),
                modificationContext -> modificationContext.getGenerationSettings().removeStructure(StructureFeature.STRONGHOLD));

        // Add Better Strongholds to all applicable biomes
        BiomeModifications.create(new ResourceLocation(BetterStrongholds.MOD_ID, "better_stronghold_addition"))
            .add(ModificationPhase.ADDITIONS,
                biomeSelectionContext -> !BetterStrongholds.blacklistedBiomes.contains(biomeSelectionContext.getBiomeKey().location().toString()),
                modificationContext -> modificationContext.getGenerationSettings().addBuiltInStructure(BSModConfiguredStructures.CONFIGURED_BETTER_STRONGHOLD));
    }

    /**
     * Restrict the dimensions our structure can spawn in.
     *
     * @author TelepathicGrunt
     */
    public static void enforceDimensionWhitelist() {
        // This is for making sure our ServerWorldEvents.LOAD event always fires after Fabric API's so our changes don't get overwritten
        ResourceLocation runAfterFabricAPIPhase = new ResourceLocation(BetterStrongholds.MOD_ID, "run_after_fabric_api");
        ServerWorldEvents.LOAD.addPhaseOrdering(Event.DEFAULT_PHASE, runAfterFabricAPIPhase);

        ServerWorldEvents.LOAD.register(runAfterFabricAPIPhase, (MinecraftServer minecraftServer, ServerLevel serverLevel) -> {
            // Grab this generator's structure settings
            StructureSettings structureSettings = serverLevel.getChunkSource().getGenerator().getSettings();

            // Create temporary mutable copy of structure config
            Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(structureSettings.structureConfig());

            // Enforce dimension whitelist
            if (BetterStrongholds.whitelistedDimensions.contains(serverLevel.dimension().location().toString())) {
                tempMap.putIfAbsent(BSModStructureFeatures.BETTER_STRONGHOLD, BSModStructureFeatures.BETTER_STRONGHOLD_STRUCTURE_CONFIG);
            } else {
                tempMap.remove(BSModStructureFeatures.BETTER_STRONGHOLD);
            }

            ((StructureSettingsAccessor) structureSettings).setStructureConfig(tempMap);
        });
    }
}
