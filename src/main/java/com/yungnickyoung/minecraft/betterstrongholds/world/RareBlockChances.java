package com.yungnickyoung.minecraft.betterstrongholds.world;


import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholds;
import com.yungnickyoung.minecraft.yungsapi.world.BlockSetSelector;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

import java.util.Random;

/**
 * Singleton class holding map of rare blocks to probabilities.
 * The class is a singleton so that it may be synchronized with the JSON file as a single source of truth.
 * If no JSON exists, this class will be populated with the default values shown below
 * (and a JSON with the default values created)
 */
public class RareBlockChances {
    /** Singleton stuff **/

    public static RareBlockChances instance; // This technically shouldn't be public, but it is necessary for loading data from JSON
    public static RareBlockChances get() {
        if (instance == null) {
            instance = new RareBlockChances();
        }
        return instance;
    }

    private RareBlockChances() {
        blockChances = new BlockSetSelector(Blocks.IRON_BLOCK.getDefaultState())
            .addBlock(Blocks.IRON_BLOCK.getDefaultState(), .3f)
            .addBlock(Blocks.QUARTZ_BLOCK.getDefaultState(), .3f)
            .addBlock(Blocks.GOLD_BLOCK.getDefaultState(), .3f)
            .addBlock(Blocks.DIAMOND_BLOCK.getDefaultState(), .1f);
    }

    /** Instance variables and methods **/

    private BlockSetSelector blockChances;

    public BlockState getRandomRareBlock(Random random) {
        // Initial check to warn user for improper summation
        float sum = blockChances.getEntries().values().stream().reduce(0f, Float::sum);
        if (sum != 1f) {
            BetterStrongholds.LOGGER.error("Your rare block spawn chances don't add up to 1! Rare blocks won't spawn as you intend!");
        }

        return blockChances.get(random);
    }
}
