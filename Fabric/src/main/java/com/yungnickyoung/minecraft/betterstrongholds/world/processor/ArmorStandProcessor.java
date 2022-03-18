package com.yungnickyoung.minecraft.betterstrongholds.world.processor;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholdsCommon;
import com.yungnickyoung.minecraft.betterstrongholds.module.StructureProcessorModule;
import com.yungnickyoung.minecraft.betterstrongholds.world.ArmorStandChances;
import com.yungnickyoung.minecraft.yungsapi.world.processor.StructureEntityProcessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

/**
 * Gives armor stands random armor depending on the type of armor
 * they are already wearing.
 */
public class ArmorStandProcessor extends StructureEntityProcessor {
    public static final ArmorStandProcessor INSTANCE = new ArmorStandProcessor();
    public static final Codec<ArmorStandProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public StructureTemplate.StructureEntityInfo processEntity(ServerLevelAccessor serverLevelAccessor,
                                                               BlockPos structurePiecePos,
                                                               BlockPos structurePieceBottomCenterPos,
                                                               StructureTemplate.StructureEntityInfo localEntityInfo,
                                                               StructureTemplate.StructureEntityInfo globalEntityInfo,
                                                               StructurePlaceSettings structurePlaceSettings) {
        if (globalEntityInfo.nbt.getString("id").equals("minecraft:armor_stand")) {
            ListTag armorItems = globalEntityInfo.nbt.getList("ArmorItems", 10);
            Random random = structurePlaceSettings.getRandom(globalEntityInfo.blockPos);

            if (globalEntityInfo.blockPos.getX() == 8628 && globalEntityInfo.blockPos.getY() == 30 && globalEntityInfo.blockPos.getZ() == -2161) {
                BetterStrongholdsCommon.LOGGER.info("halp");
            }

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
                    ? Registry.ITEM.getKey(ArmorStandChances.get().getRareBoots(random)).toString()
                    : Registry.ITEM.getKey(ArmorStandChances.get().getCommonBoots(random)).toString();
            ((CompoundTag)newNBT.getList("ArmorItems", 10).get(0)).putString("id", bootsString);
            ((CompoundTag)newNBT.getList("ArmorItems", 10).get(0)).putByte("Count", (byte) 1);
            CompoundTag bootsTagNBT = new CompoundTag();
            bootsTagNBT.putInt("Damage", 0);
            ((CompoundTag)newNBT.getList("ArmorItems", 10).get(0)).put("tag", bootsTagNBT);

            // Leggings
            String leggingsString = isRare
                    ? Registry.ITEM.getKey(ArmorStandChances.get().getRareLeggings(random)).toString()
                    : Registry.ITEM.getKey(ArmorStandChances.get().getCommonLeggings(random)).toString();
            ((CompoundTag)newNBT.getList("ArmorItems", 10).get(1)).putString("id", leggingsString);
            ((CompoundTag)newNBT.getList("ArmorItems", 10).get(1)).putByte("Count", (byte) 1);
            CompoundTag leggingsTagNBT = new CompoundTag();
            leggingsTagNBT.putInt("Damage", 0);
            ((CompoundTag)newNBT.getList("ArmorItems", 10).get(1)).put("tag", leggingsTagNBT);

            // Chestplate
            String chesplateString = isRare
                    ? Registry.ITEM.getKey(ArmorStandChances.get().getRareChestplate(random)).toString()
                    : Registry.ITEM.getKey(ArmorStandChances.get().getCommonChestplate(random)).toString();
            ((CompoundTag)newNBT.getList("ArmorItems", 10).get(2)).putString("id", chesplateString);
            ((CompoundTag)newNBT.getList("ArmorItems", 10).get(2)).putByte("Count", (byte) 1);
            CompoundTag chestplateTagNBT = new CompoundTag();
            chestplateTagNBT.putInt("Damage", 0);
            ((CompoundTag)newNBT.getList("ArmorItems", 10).get(2)).put("tag", chestplateTagNBT);

            // Helmet
            String helmetString = isRare
                    ? Registry.ITEM.getKey(ArmorStandChances.get().getRareHelmet(random)).toString()
                    : Registry.ITEM.getKey(ArmorStandChances.get().getCommonHelmet(random)).toString();
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
        return StructureProcessorModule.ARMORSTAND_PROCESSOR;
    }
}