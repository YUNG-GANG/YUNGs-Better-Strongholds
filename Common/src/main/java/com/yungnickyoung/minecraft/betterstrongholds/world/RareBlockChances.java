package com.yungnickyoung.minecraft.betterstrongholds.world;

import com.yungnickyoung.minecraft.yungsapi.world.BlockStateRandomizer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

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
        blockChances = new BlockStateRandomizer(Blocks.IRON_BLOCK.defaultBlockState())
            .addBlock(Blocks.IRON_BLOCK.defaultBlockState(), .3f)
            .addBlock(Blocks.QUARTZ_BLOCK.defaultBlockState(), .3f)
            .addBlock(Blocks.GOLD_BLOCK.defaultBlockState(), .3f)
            .addBlock(Blocks.DIAMOND_BLOCK.defaultBlockState(), .1f);
    }

    /** Instance variables and methods **/

    private BlockStateRandomizer blockChances;

    public BlockState getRandomRareBlock(Random random) {
        return blockChances.get(random);
    }
}
