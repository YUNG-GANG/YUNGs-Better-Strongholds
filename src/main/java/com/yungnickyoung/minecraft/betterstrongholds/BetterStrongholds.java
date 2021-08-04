package com.yungnickyoung.minecraft.betterstrongholds;

import com.google.common.collect.Lists;
import com.yungnickyoung.minecraft.betterstrongholds.config.BSConfig;
import com.yungnickyoung.minecraft.betterstrongholds.init.BSModConfig;
import com.yungnickyoung.minecraft.betterstrongholds.init.BSModConfiguredStructures;
import com.yungnickyoung.minecraft.betterstrongholds.init.BSModProcessors;
import com.yungnickyoung.minecraft.betterstrongholds.init.BSModStructures;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.gen.chunk.StructureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BetterStrongholds implements ModInitializer, DedicatedServerModInitializer, ClientModInitializer {
    public static final String MOD_ID = "betterstrongholds";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    /** Better Strongholds config. Uses AutoConfig. **/
    public static BSConfig CONFIG;

    /**
     * Lists of whitelisted dimensions and blacklisted biomes.
     * Will be reinitialized later w/ values from config.
     */
    public static List<String> whitelistedDimensions = Lists.newArrayList("minecraft:overworld");
    public static List<String> blacklistedBiomes = Lists.newArrayList(
        "minecraft:ocean", "minecraft:frozen_ocean", "minecraft:deep_ocean",
        "minecraft:warm_ocean", "minecraft:lukewarm_ocean", "minecraft:cold_ocean",
        "minecraft:deep_lukewarm_ocean", "minecraft:deep_cold_ocean", "minecraft:deep_frozen_ocean",
        "minecraft:beach", "minecraft:snowy_beach",
        "minecraft:river", "minecraft:frozen_river"
    );

    @Override
    public void onInitialize() {
        BSModConfig.init();
        BSModProcessors.init();
        BSModStructures.init();
        BSModConfiguredStructures.init();
    }

    /**
     * We must perform the following to ensure our ServerWorldEvents.LOAD event always fires
     * after Fabric API's usage of the same event. This is done so that our changes don't get overwritten
     * by the Fabric API adding structure spacings to all dimensions.
     * Credit to TelepathicGrunt.
     */
    @Override
    public void onInitializeServer() {
        enforceDimensionWhitelist();
    }

    @Override
    public void onInitializeClient() {
        enforceDimensionWhitelist();
    }

    public static void enforceDimensionWhitelist() {
        // Controls the dimension blacklisting
        ServerWorldEvents.LOAD.register((MinecraftServer minecraftServer, ServerWorld serverWorld) -> {
            // Don't spawn in non-whitelisted dimensions
            if (!BetterStrongholds.whitelistedDimensions.contains(serverWorld.getRegistryKey().getValue().toString())) {
                // We use a temp map to add our spacing because some mods handle immutable maps
                Map<StructureFeature<?>, StructureConfig> tempMap = new HashMap<>(serverWorld.getChunkManager().getChunkGenerator().getStructuresConfig().getStructures());

                // Remove Better Stronghold
                tempMap.keySet().remove(BSModStructures.BETTER_STRONGHOLD);
                serverWorld.getChunkManager().getChunkGenerator().getStructuresConfig().structures = tempMap;
            }
        });
    }
}
