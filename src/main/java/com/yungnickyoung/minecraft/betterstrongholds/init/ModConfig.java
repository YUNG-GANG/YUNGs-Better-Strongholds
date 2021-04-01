package com.yungnickyoung.minecraft.betterstrongholds.init;

import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholds;
import com.yungnickyoung.minecraft.betterstrongholds.config.BSConfig;
import com.yungnickyoung.minecraft.betterstrongholds.config.BSSettings;
import com.yungnickyoung.minecraft.betterstrongholds.world.OreChances;
import com.yungnickyoung.minecraft.betterstrongholds.world.RareBlockChances;
import com.yungnickyoung.minecraft.yungsapi.io.JSON;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ModConfig {
    public static void init() {
        initCustomFiles();
        // Register mod config with Forge
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, BSConfig.SPEC, "betterstrongholds-forge-1_16.toml");
        // Refresh JSON config on world load so that user doesn't have to restart MC
        MinecraftForge.EVENT_BUS.addListener(ModConfig::onWorldLoad);
    }

    private static void onWorldLoad(WorldEvent.Load event) {
        loadOresJSON();
        loadRareBlocksJSON();
    }

    private static void initCustomFiles() {
        createDirectory();
        createBaseReadMe();
        createJsonReadMe();
        loadOresJSON();
        loadRareBlocksJSON();
    }

    private static void createDirectory() {
        File parentDir = new File(FMLPaths.CONFIGDIR.get().toString(), BSSettings.CUSTOM_CONFIG_PATH);
        File customConfigDir = new File(parentDir, BSSettings.VERSION_PATH);
        try {
            String filePath = customConfigDir.getCanonicalPath();
            if (customConfigDir.mkdirs()) {
                BetterStrongholds.LOGGER.info("Creating directory for additional Better Strongholds configuration at {}", filePath);
            }
        } catch (IOException e) {
            BetterStrongholds.LOGGER.error("ERROR creating Better Strongholds config directory: {}", e.toString());
        }
    }

    private static void createBaseReadMe() {
        Path path = Paths.get(FMLPaths.CONFIGDIR.get().toString(), BSSettings.CUSTOM_CONFIG_PATH, "README.txt");
        File readme = new File(path.toString());
        if (!readme.exists()) {
            String readmeText =
                "This directory is for a few additional options for YUNG's Better Strongholds.\n" +
                "Options provided may vary by version.\n" +
                "This directory contains subdirectories for supported versions. The first time you run Better Strongholds, a version subdirectory will be created if that version supports advanced options.\n" +
                "For example, the first time you use Better Strongholds for MC 1.16 on Forge, the 'forge-1_16' subdirectory will be created in this folder.\n" +
                "If no subdirectory for your version is created, then that version probably does not support the additional options.\n\n +" +
                "NOTE -- MOST OPTIONS CAN BE FOUND IN A CONFIG FILE OUTSIDE THIS FOLDER!\n" +
                "For example, on Forge 1.16 the file is 'betterstrongholds-forge-1_16.toml'.";
            try {
                Files.write(path, readmeText.getBytes());
            } catch (IOException e) {
                BetterStrongholds.LOGGER.error("Unable to create README file!");
            }
        }
    }

    private static void createJsonReadMe() {
        Path path = Paths.get(FMLPaths.CONFIGDIR.get().toString(), BSSettings.CUSTOM_CONFIG_PATH, BSSettings.VERSION_PATH, "README.txt");
        File readme = new File(path.toString());
        if (!readme.exists()) {
            String readmeText =
                "######################################\n" +
                "#             ores.json              #\n" +
                "######################################\n\n" +
                "  This file contains a BlockSetSelector (see below) describing the probability of a given ore being chosen.\n" +
                "These probabilities are used in treasure rooms in the stronghold, in which\n" +
                "piles of ore have a chance of spawning.\n\n" +
                "  Be VERY SURE the probabilities you provide add up to 1 (i.e., 100%), or else\n" +
                "you may get unexpected results!\n\n" +
                "  For information on BlockSetSelectors, see the bottom of this README.\n\n" +
                "######################################\n" +
                "#          rareblocks.json           #\n" +
                "######################################\n\n" +
                "  This file contains a BlockSetSelector describing the probability of a given block being chosen.\n" +
                "These probabilities are used in grand libraries, in which\n" +
                "two rare blocks will spawn.\n\n" +
                "  Be VERY SURE the probabilities you provide add up to 1 (i.e., 100%), or else\n" +
                "you may get unexpected results!\n\n" +
                "  For information on BlockSetSelectors, see the bottom of this README.\n\n" +
                "######################################\n" +
                "#         BlockSetSelectors          #\n" +
                "######################################\n\n" +
                "BlockSelector: Describes a set of blocks and the probability of each block being chosen.\n" +
                " - entries: An object where each entry's key is a block, and each value is that block's probability of being chosen.\n" +
                "      The total sum of all probabilities SHOULD NOT exceed 1.0!\n" +
                " - defaultBlock: The block used for any leftover probability ranges.\n" +
                "      For example, if the total sum of all the probabilities of the entries is 0.6, then\n" +
                "      there is a 0.4 chance of the defaultBlock being selected.\n" +
                "\n" +
                "Here's an example block selector:\n" +
                "\"entries\": {\n" +
                "  \"minecraft:cobblestone\": 0.25,\n" +
                "  \"minecraft:air\": 0.2,\n" +
                "  \"minecraft:stonebrick[variant=stonebrick]\": 0.1\n" +
                "},\n" +
                "\"defaultBlock\": \"minecraft:planks[variant=oak]\"\n" +
                "\n" +
                "For each block, this selector has a 25% chance of returning cobblestone, 20% chance of choosing air,\n" +
                "10% chance of choosing stone bricks, and a 100 - (25 + 20 + 10) = 45% chance of choosing oak planks (since it's the default block).\n";
            try {
                Files.write(path, readmeText.getBytes());
            } catch (IOException e) {
                BetterStrongholds.LOGGER.error("Unable to create ores and rare blocks README file!");
            }
        }
    }

    /**
     * If a JSON file already exists, it loads its contents into OreChances.
     * Otherwise, it creates a default JSON and from the default options in OreChances.
     */
    private static void loadOresJSON() {
        Path jsonPath = Paths.get(FMLPaths.CONFIGDIR.get().toString(), BSSettings.CUSTOM_CONFIG_PATH, BSSettings.VERSION_PATH, "ores.json");
        File jsonFile = new File(jsonPath.toString());

        if (!jsonFile.exists()) {
            // Create default file if JSON file doesn't already exist
            try {
                JSON.createJsonFileFromObject(jsonPath, OreChances.get());
            } catch (IOException e) {
                BetterStrongholds.LOGGER.error("Unable to create ores.json file: {}", e.toString());
            }
        } else {
            // If file already exists, load data into BlockSetSelectors' singleton instance
            if (!jsonFile.canRead()) {
                BetterStrongholds.LOGGER.error("Better Strongholds ores.json file not readable! Using default configuration...");
                return;
            }

            try {
                OreChances.instance = JSON.loadObjectFromJsonFile(jsonPath, OreChances.class);
            } catch (IOException e) {
                BetterStrongholds.LOGGER.error("Error loading Better Strongholds ores.json file: {}", e.toString());
                BetterStrongholds.LOGGER.error("Using default configuration...");
            }
        }
    }

    /**
     * If a JSON file already exists, it loads its contents into RareBlockChances.
     * Otherwise, it creates a default JSON and from the default options in RareBlockChances.
     */
    private static void loadRareBlocksJSON() {
        Path jsonPath = Paths.get(FMLPaths.CONFIGDIR.get().toString(), BSSettings.CUSTOM_CONFIG_PATH, BSSettings.VERSION_PATH, "rareblocks.json");
        File jsonFile = new File(jsonPath.toString());

        if (!jsonFile.exists()) {
            // Create default file if JSON file doesn't already exist
            try {
                JSON.createJsonFileFromObject(jsonPath, RareBlockChances.get());
            } catch (IOException e) {
                BetterStrongholds.LOGGER.error("Unable to create rareblocks.json file: {}", e.toString());
            }
        } else {
            // If file already exists, load data into BlockSetSelectors' singleton instance
            if (!jsonFile.canRead()) {
                BetterStrongholds.LOGGER.error("Better Strongholds rareblocks.json file not readable! Using default configuration...");
                return;
            }

            try {
                RareBlockChances.instance = JSON.loadObjectFromJsonFile(jsonPath, RareBlockChances.class);
            } catch (IOException e) {
                BetterStrongholds.LOGGER.error("Error loading Better Strongholds rareblocks.json file: {}", e.toString());
                BetterStrongholds.LOGGER.error("Using default configuration...");
            }
        }
    }
}
