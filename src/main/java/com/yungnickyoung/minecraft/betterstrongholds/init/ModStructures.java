package com.yungnickyoung.minecraft.betterstrongholds.init;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholds;
import com.yungnickyoung.minecraft.betterstrongholds.world.BetterStrongholdStructure;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

public class ModStructures {
    public static final DeferredRegister<Structure<?>> DEFERRED_REGISTRY = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, BetterStrongholds.MOD_ID);
    public static final RegistryObject<Structure<NoFeatureConfig>> BETTER_STRONGHOLD = DEFERRED_REGISTRY.register("stronghold", () -> new BetterStrongholdStructure(NoFeatureConfig.field_236558_a_));

    public static void init() {
        // Register our deferred registry
        DEFERRED_REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());

        // Register event listeners
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModStructures::commonSetup);
        MinecraftForge.EVENT_BUS.addListener(ModStructures::addDimensionalSpacing);
        MinecraftForge.EVENT_BUS.addListener(ModStructures::onBiomeLoad); // We use normal priority since we are both removing and adding stuff
    }

    /**
     * Set up Better Stronghold structure.
     */
    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // Add stronghold to the structure map
            Structure.NAME_STRUCTURE_BIMAP.put("Better Stronghold".toLowerCase(Locale.ROOT), BETTER_STRONGHOLD.get());

            // Add structure + spacing settings to default dimension structures.
            // Note that we make a similar change in the WorldEvent.Load handler
            // as a safety for custom dimension support.
            DimensionStructuresSettings.field_236191_b_ =
                ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
                    .putAll(DimensionStructuresSettings.field_236191_b_)
                    .put(BETTER_STRONGHOLD.get(), new StructureSeparationSettings(85, 50, 596441294))
                    .build();

            // Register the configured structure feature
            Registry.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(BetterStrongholds.MOD_ID, "stronghold"), ModConfiguredStructures.CONFIGURED_BETTER_STRONGHOLD);

            // Add structure feature to this to prevent any issues if other mods' custom ChunkGenerators use FlatGenerationSettings.STRUCTURES
            FlatGenerationSettings.STRUCTURES.put(BETTER_STRONGHOLD.get(), ModConfiguredStructures.CONFIGURED_BETTER_STRONGHOLD);

            // Register separation settings for mods that might need it, like Terraforged
            WorldGenRegistries.NOISE_SETTINGS.getEntries().forEach(settings -> {
                Map<Structure<?>, StructureSeparationSettings> structureMap = settings.getValue().getStructures().func_236195_a_();

                // Precaution in case a mod makes the structure map immutable like datapacks do
                if (structureMap instanceof ImmutableMap){
                    Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(structureMap);
                    tempMap.put(BETTER_STRONGHOLD.get(), DimensionStructuresSettings.field_236191_b_.get(BETTER_STRONGHOLD.get()));
                    settings.getValue().getStructures().field_236193_d_ = tempMap;
                } else {
                    structureMap.put(BETTER_STRONGHOLD.get(), DimensionStructuresSettings.field_236191_b_.get(BETTER_STRONGHOLD.get()));
                }
            });
        });
    }

    /**
     * Adds the appropriate structure feature to each biome as it loads in.
     */
    private static void onBiomeLoad(BiomeLoadingEvent event) {
        // Only operate on biomes that have strongholds
        boolean found = false;
        for (Supplier<StructureFeature<?, ?>> supplier : event.getGeneration().getStructures()) {
            if (supplier.get().field_236268_b_ == Structure.STRONGHOLD) {
                found = true;
                break;
            }
        }

        if (!found) return;

        // Remove vanilla stronghold from biome generation settings.
        // This will prevent them from spawning, although the /locate entry will still exist.
        event.getGeneration().getStructures().removeIf(supplier -> supplier.get().field_236268_b_ == Structure.STRONGHOLD);

        // Add stronghold to biome generation settings
        event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_BETTER_STRONGHOLD);
    }

    /**
     * We must manually add the separation settings for our structure to spawn.
     */
    private static Method GETCODEC_METHOD; // Cached instance since this will never change once initialized

    @SuppressWarnings("unchecked")
    private static void addDimensionalSpacing(final WorldEvent.Load event) {
        if (event.getWorld() instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) event.getWorld();

            // Skip Terraforged's chunk generator as they are a special case of a mod locking down their chunkgenerator.
            // Credits to TelepathicGrunt for this.
            try {
                if (GETCODEC_METHOD == null) {
                    GETCODEC_METHOD = ObfuscationReflectionHelper.findMethod(ChunkGenerator.class, "func_230347_a_");
                }

                ResourceLocation chunkGenResourceLocation  = Registry.CHUNK_GENERATOR_CODEC.getKey((Codec<? extends ChunkGenerator>) GETCODEC_METHOD.invoke(serverWorld.getChunkProvider().generator));
                if (chunkGenResourceLocation  != null && chunkGenResourceLocation .getNamespace().equals("terraforged")) {
                    return;
                }
            } catch (Exception e) {
                BetterStrongholds.LOGGER.error("Was unable to check if " + serverWorld.getDimensionKey().getLocation() + " is using Terraforged's ChunkGenerator.");
            }

            // Prevent spawning in superflat world
            if (serverWorld.getChunkProvider().getChunkGenerator() instanceof FlatChunkGenerator && serverWorld.getDimensionKey().equals(World.OVERWORLD)) {
                return;
            }

            // We use a temp map because some mods handle immutable maps.
            Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(serverWorld.getChunkProvider().generator.func_235957_b_().func_236195_a_());
            tempMap.put(ModStructures.BETTER_STRONGHOLD.get(), DimensionStructuresSettings.field_236191_b_.get(ModStructures.BETTER_STRONGHOLD.get()));
            serverWorld.getChunkProvider().generator.func_235957_b_().field_236193_d_ = tempMap;
        }
    }
}