package com.yungnickyoung.minecraft.betterstrongholds.world.processor;

import com.mojang.serialization.MapCodec;
import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholdsCommon;
import com.yungnickyoung.minecraft.betterstrongholds.module.StructureProcessorTypeModule;
import com.yungnickyoung.minecraft.betterstrongholds.world.ArmorStandChances;
import com.yungnickyoung.minecraft.yungsapi.world.processor.StructureEntityProcessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;

/**
 * Gives armor stands random armor depending on the type of armor
 * they are already wearing.
 */
public class ArmorStandProcessor extends StructureEntityProcessor {
    public static final ArmorStandProcessor INSTANCE = new ArmorStandProcessor();
    public static final MapCodec<StructureProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    @Override
    public StructureTemplate.StructureEntityInfo processEntity(ServerLevelAccessor serverLevelAccessor,
                                                               BlockPos structurePiecePos,
                                                               BlockPos structurePieceBottomCenterPos,
                                                               StructureTemplate.StructureEntityInfo localEntityInfo,
                                                               StructureTemplate.StructureEntityInfo globalEntityInfo,
                                                               StructurePlaceSettings structurePlaceSettings) {
        if (globalEntityInfo.nbt.getString("id").equals("minecraft:armor_stand")) {
            ListTag armorItems = globalEntityInfo.nbt.getList("ArmorItems", 10);
            RandomSource random = structurePlaceSettings.getRandom(globalEntityInfo.blockPos);

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
                    ? BuiltInRegistries.ITEM.getKey(ArmorStandChances.get().getRareBoots(random)).toString()
                    : BuiltInRegistries.ITEM.getKey(ArmorStandChances.get().getCommonBoots(random)).toString();
            if (!bootsString.equals("minecraft:air")) {
                ((CompoundTag) newNBT.getList("ArmorItems", 10).get(0)).putString("id", bootsString);
                ((CompoundTag) newNBT.getList("ArmorItems", 10).get(0)).putByte("Count", (byte) 1);
                CompoundTag bootsTagNBT = new CompoundTag();
                bootsTagNBT.putInt("Damage", 0);
                ((CompoundTag) newNBT.getList("ArmorItems", 10).get(0)).put("tag", bootsTagNBT);
            }

            // Leggings
            String leggingsString = isRare
                    ? BuiltInRegistries.ITEM.getKey(ArmorStandChances.get().getRareLeggings(random)).toString()
                    : BuiltInRegistries.ITEM.getKey(ArmorStandChances.get().getCommonLeggings(random)).toString();
            if (!leggingsString.equals("minecraft:air")) {
                ((CompoundTag) newNBT.getList("ArmorItems", 10).get(1)).putString("id", leggingsString);
                ((CompoundTag) newNBT.getList("ArmorItems", 10).get(1)).putByte("Count", (byte) 1);
                CompoundTag leggingsTagNBT = new CompoundTag();
                leggingsTagNBT.putInt("Damage", 0);
                ((CompoundTag) newNBT.getList("ArmorItems", 10).get(1)).put("tag", leggingsTagNBT);
            }

            // Chestplate
            String chestplateString = isRare
                    ? BuiltInRegistries.ITEM.getKey(ArmorStandChances.get().getRareChestplate(random)).toString()
                    : BuiltInRegistries.ITEM.getKey(ArmorStandChances.get().getCommonChestplate(random)).toString();
            if (!chestplateString.equals("minecraft:air")) {
                ((CompoundTag) newNBT.getList("ArmorItems", 10).get(2)).putString("id", chestplateString);
                ((CompoundTag) newNBT.getList("ArmorItems", 10).get(2)).putByte("Count", (byte) 1);
                CompoundTag chestplateTagNBT = new CompoundTag();
                chestplateTagNBT.putInt("Damage", 0);
                ((CompoundTag) newNBT.getList("ArmorItems", 10).get(2)).put("tag", chestplateTagNBT);
            }

            // Helmet
            String helmetString = isRare
                    ? BuiltInRegistries.ITEM.getKey(ArmorStandChances.get().getRareHelmet(random)).toString()
                    : BuiltInRegistries.ITEM.getKey(ArmorStandChances.get().getCommonHelmet(random)).toString();
            if (!helmetString.equals("minecraft:air")) {
                ((CompoundTag) newNBT.getList("ArmorItems", 10).get(3)).putString("id", helmetString);
                ((CompoundTag) newNBT.getList("ArmorItems", 10).get(3)).putByte("Count", (byte) 1);
                CompoundTag helmetTagNBT = new CompoundTag();
                helmetTagNBT.putInt("Damage", 0);
                ((CompoundTag) newNBT.getList("ArmorItems", 10).get(3)).put("tag", helmetTagNBT);
                globalEntityInfo = new StructureTemplate.StructureEntityInfo(globalEntityInfo.pos, globalEntityInfo.blockPos, newNBT);
            }
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