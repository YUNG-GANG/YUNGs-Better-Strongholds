package com.yungnickyoung.minecraft.betterstrongholds.world;

import com.yungnickyoung.minecraft.yungsapi.world.BlockStateRandomizer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

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
        oreChances = new BlockStateRandomizer(Blocks.COAL_ORE.defaultBlockState())
            .addBlock(Blocks.COAL_ORE.defaultBlockState(), .2f)
            .addBlock(Blocks.IRON_ORE.defaultBlockState(), .2f)
            .addBlock(Blocks.GOLD_ORE.defaultBlockState(), .2f)
            .addBlock(Blocks.LAPIS_ORE.defaultBlockState(), .15f)
            .addBlock(Blocks.REDSTONE_ORE.defaultBlockState(), .15f)
            .addBlock(Blocks.EMERALD_ORE.defaultBlockState(), .05f)
            .addBlock(Blocks.DIAMOND_ORE.defaultBlockState(), .05f);
    }

    /** Instance variables and methods **/

    private BlockStateRandomizer oreChances;

    public BlockState getRandomOre(Random random) {
        return oreChances.get(random);
    }
}
