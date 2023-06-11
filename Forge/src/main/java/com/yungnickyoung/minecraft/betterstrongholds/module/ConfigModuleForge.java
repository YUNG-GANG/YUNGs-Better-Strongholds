package com.yungnickyoung.minecraft.betterstrongholds.module;

import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholdsCommon;
import com.yungnickyoung.minecraft.betterstrongholds.config.BSConfigForge;
import com.yungnickyoung.minecraft.betterstrongholds.world.ArmorStandChances;
import com.yungnickyoung.minecraft.betterstrongholds.world.ItemFrameChances;
import com.yungnickyoung.minecraft.betterstrongholds.world.OreChances;
import com.yungnickyoung.minecraft.betterstrongholds.world.RareBlockChances;
import com.yungnickyoung.minecraft.yungsapi.io.JSON;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigModuleForge {
    public static final String CUSTOM_CONFIG_PATH = "betterstrongholds";
    public static final String VERSION_PATH = "forge-1_20";

    public static void init() {
        initCustomFiles();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BSConfigForge.SPEC, "betterstrongholds-forge-1_20.toml");
        MinecraftForge.EVENT_BUS.addListener(ConfigModuleForge::onWorldLoad);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ConfigModuleForge::onConfigChange);
    }

    private static void onWorldLoad(LevelEvent.Load event) {
        bakeConfig();
        loadJSON();
    }

    private static void onConfigChange(ModConfigEvent event) {
        if (event.getConfig().getSpec() == BSConfigForge.SPEC) {
            bakeConfig();
            loadJSON();
        }
    }

    private static void initCustomFiles() {
        createDirectory();
        createBaseReadMe();
        createJsonReadMe();
        loadJSON();
    }

    private static void loadJSON() {
        loadOresJSON();
        loadRareBlocksJSON();
        loadArmorStandsJSON();
        loadItemFramesJSON();
    }

    private static void createDirectory() {
        File parentDir = new File(FMLPaths.CONFIGDIR.get().toString(), CUSTOM_CONFIG_PATH);
        File customConfigDir = new File(parentDir, VERSION_PATH);
        try {
            String filePath = customConfigDir.getCanonicalPath();
            if (customConfigDir.mkdirs()) {
                BetterStrongholdsCommon.LOGGER.info("Creating directory for additional Better Strongholds configuration at {}", filePath);
            }
        } catch (IOException e) {
            BetterStrongholdsCommon.LOGGER.error("ERROR creating Better Strongholds config directory: {}", e.toString());
        }
    }

    private static void createBaseReadMe() {
        Path path = Paths.get(FMLPaths.CONFIGDIR.get().toString(), CUSTOM_CONFIG_PATH, "README.txt");
        File readme = new File(path.toString());
        if (!readme.exists()) {
            String readmeText =
                    """
                            This directory is for a few additional options for YUNG's Better Strongholds.
                            Options provided may vary by version.
                            This directory contains subdirectories for supported versions. The first time you run Better Strongholds, a version subdirectory will be created if that version supports advanced options.
                            For example, the first time you use Better Strongholds for MC 1.16 on Forge, the 'forge-1_16' subdirectory will be created in this folder.
                            If no subdirectory for your version is created, then that version probably does not support the additional options.

                            NOTE -- MOST OPTIONS CAN BE FOUND IN A CONFIG FILE OUTSIDE THIS FOLDER!
                            For example, on Forge 1.16 the file is 'betterstrongholds-forge-1_16.toml'.""";
            try {
                Files.write(path, readmeText.getBytes());
            } catch (IOException e) {
                BetterStrongholdsCommon.LOGGER.error("Unable to create README file!");
            }
        }
    }

    private static void createJsonReadMe() {
        Path path = Paths.get(FMLPaths.CONFIGDIR.get().toString(), CUSTOM_CONFIG_PATH, VERSION_PATH, "README.txt");
        File readme = new File(path.toString());
        if (!readme.exists()) {
            String readmeText =
                    """
                            ######################################
                            #             ores.json              #
                            ######################################

                              This file contains a BlockSetSelector (see below) describing the probability of a given ore being chosen.
                            These probabilities are used in treasure rooms in the stronghold, in which
                            piles of ore have a chance of spawning.
                            For information on BlockSetSelectors, see the bottom of this README.

                            ######################################
                            #          rareblocks.json           #
                            ######################################

                              This file contains a BlockSetSelector describing the probability of a given block being chosen.
                            These probabilities are used in grand libraries, in which
                            two rare blocks will spawn.
                            For information on BlockSetSelectors, see the bottom of this README.

                            ######################################
                            #          armorstands.json          #
                            ######################################

                              This file contains ItemSetSelectors describing the probability distribution of armor on armor stands.
                            Common armor stands spawn in Armoury rooms, while Rare ones are only available in the rare Commander rooms.
                            For information on ItemSetSelectors, see the bottom of this README.

                            ######################################
                            #          itemframes.json          #
                            ######################################

                              This file contains ItemSetSelectors describing the probability distribution of items in item frames.
                            Item frames only spawn in storage rooms and armoury rooms.
                            For information on ItemSetSelectors, see the bottom of this README.

                            ######################################
                            #         BlockSetSelectors          #
                            ######################################

                            Describes a set of blockstates and the probability of each blockstate being chosen.
                             - entries: An object where each entry's key is a blockstate, and each value is that blockstate's probability of being chosen.
                                  The total sum of all probabilities SHOULD NOT exceed 1.0!
                             - defaultBlock: The blockstate used for any leftover probability ranges.
                                  For example, if the total sum of all the probabilities of the entries is 0.6, then
                                  there is a 0.4 chance of the defaultBlock being selected.

                            Here's an example block selector:
                            "entries": {
                              "minecraft:cobblestone": 0.25,
                              "minecraft:air": 0.2,
                              "minecraft:stone_bricks": 0.1
                            },
                            "defaultBlock": "minecraft:oak_planks"

                            For each block, this selector has a 25% chance of returning cobblestone, 20% chance of choosing air,
                            10% chance of choosing stone bricks, and a 100 - (25 + 20 + 10) = 45% chance of choosing oak planks (since it's the default block).

                            ######################################
                            #         ItemSetSelectors           #
                            ######################################

                            Describes a set of items and the probability of each item being chosen.
                            Works the same as BlockSetSelectors, but with items instead of blockstates.
                            """;

            try {
                Files.write(path, readmeText.getBytes());
            } catch (IOException e) {
                BetterStrongholdsCommon.LOGGER.error("Unable to create ores and rare blocks README file!");
            }
        }
    }

    /**
     * If a JSON file already exists, it loads its contents into OreChances.
     * Otherwise, it creates a default JSON and from the default options in OreChances.
     */
    private static void loadOresJSON() {
        Path jsonPath = Paths.get(FMLPaths.CONFIGDIR.get().toString(), CUSTOM_CONFIG_PATH, VERSION_PATH, "ores.json");
        File jsonFile = new File(jsonPath.toString());

        if (!jsonFile.exists()) {
            // Create default file if JSON file doesn't already exist
            try {
                JSON.createJsonFileFromObject(jsonPath, OreChances.get());
            } catch (IOException e) {
                BetterStrongholdsCommon.LOGGER.error("Unable to create ores.json file: {}", e.toString());
            }
        } else {
            // If file already exists, load data into OreChances singleton instance
            if (!jsonFile.canRead()) {
                BetterStrongholdsCommon.LOGGER.error("Better Strongholds ores.json file not readable! Using default configuration...");
                return;
            }

            try {
                OreChances.instance = JSON.loadObjectFromJsonFile(jsonPath, OreChances.class);
            } catch (IOException e) {
                BetterStrongholdsCommon.LOGGER.error("Error loading Better Strongholds ores.json file: {}", e.toString());
                BetterStrongholdsCommon.LOGGER.error("Using default configuration...");
            }
        }
    }

    /**
     * If a JSON file already exists, it loads its contents into RareBlockChances.
     * Otherwise, it creates a default JSON and from the default options in RareBlockChances.
     */
    private static void loadRareBlocksJSON() {
        Path jsonPath = Paths.get(FMLPaths.CONFIGDIR.get().toString(), CUSTOM_CONFIG_PATH, VERSION_PATH, "rareblocks.json");
        File jsonFile = new File(jsonPath.toString());

        if (!jsonFile.exists()) {
            // Create default file if JSON file doesn't already exist
            try {
                JSON.createJsonFileFromObject(jsonPath, RareBlockChances.get());
            } catch (IOException e) {
                BetterStrongholdsCommon.LOGGER.error("Unable to create rareblocks.json file: {}", e.toString());
            }
        } else {
            // If file already exists, load data into RareBlockChances singleton instance
            if (!jsonFile.canRead()) {
                BetterStrongholdsCommon.LOGGER.error("Better Strongholds rareblocks.json file not readable! Using default configuration...");
                return;
            }

            try {
                RareBlockChances.instance = JSON.loadObjectFromJsonFile(jsonPath, RareBlockChances.class);
            } catch (IOException e) {
                BetterStrongholdsCommon.LOGGER.error("Error loading Better Strongholds rareblocks.json file: {}", e.toString());
                BetterStrongholdsCommon.LOGGER.error("Using default configuration...");
            }
        }
    }

    /**
     * If a JSON file already exists, it loads its contents into ArmorStandChances.
     * Otherwise, it creates a default JSON and from the default options in ArmorStandChances.
     */
    private static void loadArmorStandsJSON() {
        Path jsonPath = Paths.get(FMLPaths.CONFIGDIR.get().toString(), CUSTOM_CONFIG_PATH, VERSION_PATH, "armorstands.json");
        File jsonFile = new File(jsonPath.toString());

        if (!jsonFile.exists()) {
            // Create default file if JSON file doesn't already exist
            try {
                JSON.createJsonFileFromObject(jsonPath, ArmorStandChances.get());
            } catch (IOException e) {
                BetterStrongholdsCommon.LOGGER.error("Unable to create armorstands.json file: {}", e.toString());
            }
        } else {
            // If file already exists, load data into ArmorStandChances singleton instance
            if (!jsonFile.canRead()) {
                BetterStrongholdsCommon.LOGGER.error("Better Strongholds armorstands.json file not readable! Using default configuration...");
                return;
            }

            try {
                ArmorStandChances.instance = JSON.loadObjectFromJsonFile(jsonPath, ArmorStandChances.class);
            } catch (IOException e) {
                BetterStrongholdsCommon.LOGGER.error("Error loading Better Strongholds armorstands.json file: {}", e.toString());
                BetterStrongholdsCommon.LOGGER.error("Using default configuration...");
            }
        }
    }

    /**
     * If a JSON file already exists, it loads its contents into ItemFrameChances.
     * Otherwise, it creates a default JSON and from the default options in ItemFrameChances.
     */
    private static void loadItemFramesJSON() {
        Path jsonPath = Paths.get(FMLPaths.CONFIGDIR.get().toString(), CUSTOM_CONFIG_PATH, VERSION_PATH, "itemframes.json");
        File jsonFile = new File(jsonPath.toString());

        if (!jsonFile.exists()) {
            // Create default file if JSON file doesn't already exist
            try {
                JSON.createJsonFileFromObject(jsonPath, ItemFrameChances.get());
            } catch (IOException e) {
                BetterStrongholdsCommon.LOGGER.error("Unable to create itemframes.json file: {}", e.toString());
            }
        } else {
            // If file already exists, load data into ArmorStandChances singleton instance
            if (!jsonFile.canRead()) {
                BetterStrongholdsCommon.LOGGER.error("Better Strongholds itemframes.json file not readable! Using default configuration...");
                return;
            }

            try {
                ItemFrameChances.instance = JSON.loadObjectFromJsonFile(jsonPath, ItemFrameChances.class);
            } catch (IOException e) {
                BetterStrongholdsCommon.LOGGER.error("Error loading Better Strongholds itemframes.json file: {}", e.toString());
                BetterStrongholdsCommon .LOGGER.error("Using default configuration...");
            }
        }
    }

    private static void bakeConfig() {
        BetterStrongholdsCommon.CONFIG.general.cobwebReplacementChanceNormal = BSConfigForge.general.cobwebReplacementChanceNormal.get();
        BetterStrongholdsCommon.CONFIG.general.cobwebReplacementChanceSpawner = BSConfigForge.general.cobwebReplacementChanceSpawner.get();
        BetterStrongholdsCommon.CONFIG.general.torchSpawnRate = BSConfigForge.general.torchSpawnRate.get();
        BetterStrongholdsCommon.CONFIG.general.lanternSpawnRate = BSConfigForge.general.lanternSpawnRate.get();
    }
}
