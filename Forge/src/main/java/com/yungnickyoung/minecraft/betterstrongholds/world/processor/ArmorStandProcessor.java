package com.yungnickyoung.minecraft.betterstrongholds.world.processor;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholdsCommon;
import com.yungnickyoung.minecraft.betterstrongholds.module.StructureProcessorTypeModule;
import com.yungnickyoung.minecraft.betterstrongholds.world.ArmorStandChances;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Gives armor stands random armor depending on the type of armor
 * they are already wearing.
 */
@ParametersAreNonnullByDefault
public class ArmorStandProcessor extends StructureProcessor {
    public static final ArmorStandProcessor INSTANCE = new ArmorStandProcessor();
    public static final Codec<StructureProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public StructureTemplate.StructureEntityInfo processEntity(LevelReader levelReader,
                                                               BlockPos structurePiecePos,
                                                               StructureTemplate.StructureEntityInfo localEntityInfo,
                                                               StructureTemplate.StructureEntityInfo globalEntityInfo,
                                                               StructurePlaceSettings structurePlaceSettings,
                                                               StructureTemplate template) {
        if (globalEntityInfo.nbt.getString("id").equals("minecraft:armor_stand")) {
            ListTag armorItems = globalEntityInfo.nbt.getList("ArmorItems", 10);
            RandomSource randomSource = structurePlaceSettings.getRandom(globalEntityInfo.blockPos);

            // Type depends on the helmet and nothing else
            String helmet;
            try {
                helmet = ((CompoundTag) armorItems.get(3)).get("id").toString();
            } catch(Exception e) {
                BetterStrongholdsCommon.LOGGER.info("Unable to randomize armor stand at {}. Missing helmet?", globalEntityInfo.blockPos);
                return globalEntityInfo;
            }

            boolean isRare = helmet.equals("\"minecraft:diamond_helmet\"");

            CompoundTag newNBT = globalEntityInfo.nbt.copy();
            // Boots
            String bootsString = isRare
                ? ForgeRegistries.ITEMS.getKey(ArmorStandChances.get().getRareBoots(randomSource)).toString()
                : ForgeRegistries.ITEMS.getKey(ArmorStandChances.get().getCommonBoots(randomSource)).toString();
            ((CompoundTag)newNBT.getList("ArmorItems", 10).get(0)).putString("id", bootsString);
            ((CompoundTag)newNBT.getList("ArmorItems", 10).get(0)).putByte("Count", (byte) 1);
            CompoundTag bootsTagNBT = new CompoundTag();
            bootsTagNBT.putInt("Damage", 0);
            ((CompoundTag)newNBT.getList("ArmorItems", 10).get(0)).put("tag", bootsTagNBT);

            // Leggings
            String leggingsString = isRare
                ? ForgeRegistries.ITEMS.getKey(ArmorStandChances.get().getRareLeggings(randomSource)).toString()
                : ForgeRegistries.ITEMS.getKey(ArmorStandChances.get().getCommonLeggings(randomSource)).toString();
            ((CompoundTag)newNBT.getList("ArmorItems", 10).get(1)).putString("id", leggingsString);
            ((CompoundTag)newNBT.getList("ArmorItems", 10).get(1)).putByte("Count", (byte) 1);
            CompoundTag leggingsTagNBT = new CompoundTag();
            leggingsTagNBT.putInt("Damage", 0);
            ((CompoundTag)newNBT.getList("ArmorItems", 10).get(1)).put("tag", leggingsTagNBT);

            // Chestplate
            String chesplateString = isRare
                ? ForgeRegistries.ITEMS.getKey(ArmorStandChances.get().getRareChestplate(randomSource)).toString()
                : ForgeRegistries.ITEMS.getKey(ArmorStandChances.get().getCommonChestplate(randomSource)).toString();
            ((CompoundTag)newNBT.getList("ArmorItems", 10).get(2)).putString("id", chesplateString);
            ((CompoundTag)newNBT.getList("ArmorItems", 10).get(2)).putByte("Count", (byte) 1);
            CompoundTag chestplateTagNBT = new CompoundTag();
            chestplateTagNBT.putInt("Damage", 0);
            ((CompoundTag)newNBT.getList("ArmorItems", 10).get(2)).put("tag", chestplateTagNBT);

            // Helmet
            String helmetString = isRare
                ? ForgeRegistries.ITEMS.getKey(ArmorStandChances.get().getRareHelmet(randomSource)).toString()
                : ForgeRegistries.ITEMS.getKey(ArmorStandChances.get().getCommonHelmet(randomSource)).toString();
            ((CompoundTag)newNBT.getList("ArmorItems", 10).get(3)).putString("id", helmetString);
            ((CompoundTag)newNBT.getList("ArmorItems", 10).get(3)).putByte("Count", (byte) 1);
            CompoundTag helmetTagNBT = new CompoundTag();
            helmetTagNBT.putInt("Damage", 0);
            ((CompoundTag)newNBT.getList("ArmorItems", 10).get(3)).put("tag", helmetTagNBT);
            globalEntityInfo = new StructureTemplate.StructureEntityInfo(globalEntityInfo.pos, globalEntityInfo.blockPos, newNBT);
        }
        return globalEntityInfo;
    }

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                             StructureTemplate.StructureBlockInfo blockInfoLocal,
                                                             StructureTemplate.StructureBlockInfo blockInfoGlobal,
                                                             StructurePlaceSettings structurePlacementData) {
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorTypeModule.ARMORSTAND_PROCESSOR;
    }
}
