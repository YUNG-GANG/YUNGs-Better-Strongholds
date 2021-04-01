package com.yungnickyoung.minecraft.betterstrongholds.world;

import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholds;
import com.yungnickyoung.minecraft.yungsapi.world.BlockSetSelector;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

import java.util.Random;

/**
 * Singleton class holding map of ore blocks to probabilities.
 * The class is a singleton so that it may be synchronized with the JSON file as a single source of truth.
 * If no JSON exists, this class will be populated with the default values shown below
 * (and a JSON with the default values created)
 */
public class OreChances {
    /** Singleton stuff **/

    public static OreChances instance; // This technically shouldn't be public, but it is necessary for loading data from JSON
    public static OreChances get() {
        if (instance == null) {
            instance = new OreChances();
        }
        return instance;
    }

    private OreChances() {
        oreChances = new BlockSetSelector(Blocks.COAL_ORE.getDefaultState())
            .addBlock(Blocks.COAL_ORE.getDefaultState(), .2f)
            .addBlock(Blocks.IRON_ORE.getDefaultState(), .2f)
            .addBlock(Blocks.GOLD_ORE.getDefaultState(), .2f)
            .addBlock(Blocks.LAPIS_ORE.getDefaultState(), .15f)
            .addBlock(Blocks.REDSTONE_ORE.getDefaultState(), .15f)
            .addBlock(Blocks.EMERALD_ORE.getDefaultState(), .05f)
            .addBlock(Blocks.DIAMOND_ORE.getDefaultState(), .05f);
    }

    /** Instance variables and methods **/

    private BlockSetSelector oreChances;

    public BlockState getRandomOre(Random random) {
        // Initial check to warn user for improper summation
        float sum = oreChances.getEntries().values().stream().reduce(0f, Float::sum);
        if (sum != 1f) {
            BetterStrongholds.LOGGER.error("Your ore spawn chances don't add up to 1! Ores won't spawn as you intend!");
        }

        return oreChances.get(random);
    }
}
