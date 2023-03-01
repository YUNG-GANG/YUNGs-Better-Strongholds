package com.yungnickyoung.minecraft.betterstrongholds.world;

import com.yungnickyoung.minecraft.yungsapi.api.world.randomize.ItemRandomizer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

/**
 * Singleton class holding ItemRandomizers for armor pieces on armor stands.
 * The class is a singleton so that it may be synchronized with the JSON file as a single source of truth.
 * If no JSON exists, this class will be populated with the default values shown below
 * (and a JSON with the default values created)
 */
public class ArmorStandChances {
    /** Singleton stuff **/

    public static ArmorStandChances instance; // This technically shouldn't be public, but it is necessary for loading data from JSON
    public static ArmorStandChances get() {
        if (instance == null) {
            instance = new ArmorStandChances();
        }
        return instance;
    }

    private ArmorStandChances() {
        commonHelmets = new ItemRandomizer(Items.AIR)
            .addItem(Items.CHAINMAIL_HELMET, .3f)
            .addItem(Items.LEATHER_HELMET, .1f)
            .addItem(Items.IRON_HELMET, .3f)
            .addItem(Items.CARVED_PUMPKIN, .01f);

        rareHelmets = new ItemRandomizer(Items.AIR)
            .addItem(Items.DIAMOND_HELMET, .3f)
            .addItem(Items.CARVED_PUMPKIN, .2f);

        commonChestplates = new ItemRandomizer(Items.AIR)
            .addItem(Items.CHAINMAIL_CHESTPLATE, .3f)
            .addItem(Items.LEATHER_CHESTPLATE, .1f)
            .addItem(Items.IRON_CHESTPLATE, .3f);

        rareChestplates = new ItemRandomizer(Items.AIR)
            .addItem(Items.DIAMOND_CHESTPLATE, .3f);

        commonLeggings = new ItemRandomizer(Items.AIR)
            .addItem(Items.CHAINMAIL_LEGGINGS, .3f)
            .addItem(Items.LEATHER_LEGGINGS, .1f)
            .addItem(Items.IRON_LEGGINGS, .3f);

        rareLeggings = new ItemRandomizer(Items.AIR)
            .addItem(Items.DIAMOND_LEGGINGS, .3f);

        commonBoots = new ItemRandomizer(Items.AIR)
            .addItem(Items.CHAINMAIL_BOOTS, .3f)
            .addItem(Items.LEATHER_BOOTS, .1f)
            .addItem(Items.IRON_BOOTS, .3f);

        rareBoots = new ItemRandomizer(Items.AIR)
            .addItem(Items.DIAMOND_BOOTS, .3f);
    }

    /** Instance variables and methods **/

    // Helmets
    private ItemRandomizer commonHelmets;
    private ItemRandomizer rareHelmets;

    // Chestplates
    private ItemRandomizer commonChestplates;
    private ItemRandomizer rareChestplates;

    // Leggings
    private ItemRandomizer commonLeggings;
    private ItemRandomizer rareLeggings;

    // Boots
    private ItemRandomizer commonBoots;
    private ItemRandomizer rareBoots;

    public Item getCommonHelmet(RandomSource randomSource) {
        return commonHelmets.get(randomSource);
    }

    public Item getRareHelmet(RandomSource randomSource) {
         return rareHelmets.get(randomSource);
    }

    public Item getCommonChestplate(RandomSource randomSource) {
        return commonChestplates.get(randomSource);
    }

    public Item getRareChestplate(RandomSource randomSource) {
        return rareChestplates.get(randomSource);
    }

    public Item getCommonLeggings(RandomSource randomSource) {
        return commonLeggings.get(randomSource);
    }

    public Item getRareLeggings(RandomSource randomSource) {
        return rareLeggings.get(randomSource);
    }

    public Item getCommonBoots(RandomSource randomSource) {
        return commonBoots.get(randomSource);
    }

    public Item getRareBoots(RandomSource randomSource) {
        return rareBoots.get(randomSource);
    }
}
