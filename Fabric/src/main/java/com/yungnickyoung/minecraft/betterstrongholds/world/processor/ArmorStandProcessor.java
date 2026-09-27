package com.yungnickyoung.minecraft.betterstrongholds.world.processor;

import com.mojang.serialization.MapCodec;
import com.yungnickyoung.minecraft.betterstrongholds.module.StructureProcessorTypeModule;
import com.yungnickyoung.minecraft.betterstrongholds.world.ArmorStandChances;
import com.yungnickyoung.minecraft.yungsapi.world.processor.StructureEntityProcessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
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
        if (globalEntityInfo.nbt.getStringOr("id", "").equals("minecraft:armor_stand")) {
            CompoundTag equipment = globalEntityInfo.nbt.getCompoundOrEmpty("equipment");
            RandomSource random = structurePlaceSettings.getRandom(globalEntityInfo.blockPos);

            // Type depends on the helmet and nothing else
            String helmet = equipment.getCompoundOrEmpty("head").getStringOr("id", "");

            boolean isRare = helmet.equals("minecraft:diamond_helmet");

            CompoundTag newNBT = globalEntityInfo.nbt.copy();
            if (!newNBT.contains("equipment")) {
                newNBT.put("equipment", new CompoundTag());
            }
            CompoundTag newEquipment = newNBT.getCompoundOrEmpty("equipment");
            // Boots
            String bootsString = isRare
                    ? BuiltInRegistries.ITEM.getKey(ArmorStandChances.get().getRareBoots(random)).toString()
                    : BuiltInRegistries.ITEM.getKey(ArmorStandChances.get().getCommonBoots(random)).toString();
            if (!bootsString.equals("minecraft:air")) {
                addEquipment(bootsString, newEquipment, "feet");
            }

            // Leggings
            String leggingsString = isRare
                    ? BuiltInRegistries.ITEM.getKey(ArmorStandChances.get().getRareLeggings(random)).toString()
                    : BuiltInRegistries.ITEM.getKey(ArmorStandChances.get().getCommonLeggings(random)).toString();
            if (!leggingsString.equals("minecraft:air")) {
                addEquipment(leggingsString, newEquipment, "legs");
            }

            // Chestplate
            String chestplateString = isRare
                    ? BuiltInRegistries.ITEM.getKey(ArmorStandChances.get().getRareChestplate(random)).toString()
                    : BuiltInRegistries.ITEM.getKey(ArmorStandChances.get().getCommonChestplate(random)).toString();
            if (!chestplateString.equals("minecraft:air")) {
                addEquipment(chestplateString, newEquipment, "chest");
            }

            // Helmet
            String helmetString = isRare
                    ? BuiltInRegistries.ITEM.getKey(ArmorStandChances.get().getRareHelmet(random)).toString()
                    : BuiltInRegistries.ITEM.getKey(ArmorStandChances.get().getCommonHelmet(random)).toString();
            if (!helmetString.equals("minecraft:air")) {
                addEquipment(helmetString, newEquipment, "head");
            }

            globalEntityInfo = new StructureTemplate.StructureEntityInfo(globalEntityInfo.pos, globalEntityInfo.blockPos, newNBT);
        }
        return globalEntityInfo;
    }

    private static void addEquipment(final String id, final CompoundTag newEquipment, final String key) {
        var bootsTag = new CompoundTag();
        bootsTag.putString("id", id);
        bootsTag.putInt("count", 1);
        newEquipment.put(key, bootsTag);
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                             BlockPos pivotPos,
                                                             StructureTemplate.StructureBlockInfo blockInfo,
                                                             StructurePlaceSettings structurePlacementData) {
        return blockInfo;
    }

    @Override
    public MapCodec<? extends StructureProcessor> codec() {
        return StructureProcessorTypeModule.ARMORSTAND_PROCESSOR;
    }
}