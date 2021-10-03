package com.yungnickyoung.minecraft.betterstrongholds.world.processor;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholds;
import com.yungnickyoung.minecraft.betterstrongholds.init.BSModProcessors;
import com.yungnickyoung.minecraft.betterstrongholds.world.ArmorStandChances;
import com.yungnickyoung.minecraft.yungsapi.world.processor.StructureEntityProcessor;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.WorldView;
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
    public Structure.StructureEntityInfo processEntity(WorldView worldView,
                                                       BlockPos structurePiecePos,
                                                       BlockPos structurePieceBottomCenterPos,
                                                       Structure.StructureEntityInfo localEntityInfo,
                                                       Structure.StructureEntityInfo globalEntityInfo,
                                                       StructurePlacementData structurePlacementData
    ) {
        if (globalEntityInfo.nbt.getString("id").equals("minecraft:armor_stand")) {
            NbtList armorItems = globalEntityInfo.nbt.getList("ArmorItems", 10);
            Random random = structurePlacementData.getRandom(globalEntityInfo.blockPos);

            if (globalEntityInfo.blockPos.getX() == 8628 && globalEntityInfo.blockPos.getY() == 30 && globalEntityInfo.blockPos.getZ() == -2161) {
                BetterStrongholds.LOGGER.info("halp");
            }

            // Type depends on the helmet and nothing else
            String helmet;
            try {
                helmet = ((NbtCompound) armorItems.get(3)).get("id").toString();
            } catch(Exception e) {
                BetterStrongholds.LOGGER.info("Unable to randomize armor stand at {}. Missing helmet?", globalEntityInfo.blockPos);
                return globalEntityInfo;
            }

            boolean isRare = helmet.equals("\"minecraft:diamond_helmet\"");

            NbtCompound newNBT = globalEntityInfo.nbt.copy();
            // Boots
            String bootsString = isRare
                ? Registry.ITEM.getId(ArmorStandChances.get().getRareBoots(random)).toString()
                : Registry.ITEM.getId(ArmorStandChances.get().getCommonBoots(random)).toString();
            ((NbtCompound)newNBT.getList("ArmorItems", 10).get(0)).putString("id", bootsString);
            ((NbtCompound)newNBT.getList("ArmorItems", 10).get(0)).putByte("Count", (byte) 1);
            NbtCompound bootsTagNBT = new NbtCompound();
            bootsTagNBT.putInt("Damage", 0);
            ((NbtCompound)newNBT.getList("ArmorItems", 10).get(0)).put("tag", bootsTagNBT);

            // Leggings
            String leggingsString = isRare
                ? Registry.ITEM.getId(ArmorStandChances.get().getRareLeggings(random)).toString()
                : Registry.ITEM.getId(ArmorStandChances.get().getCommonLeggings(random)).toString();
            ((NbtCompound)newNBT.getList("ArmorItems", 10).get(1)).putString("id", leggingsString);
            ((NbtCompound)newNBT.getList("ArmorItems", 10).get(1)).putByte("Count", (byte) 1);
            NbtCompound leggingsTagNBT = new NbtCompound();
            leggingsTagNBT.putInt("Damage", 0);
            ((NbtCompound)newNBT.getList("ArmorItems", 10).get(1)).put("tag", leggingsTagNBT);

            // Chestplate
            String chesplateString = isRare
                ? Registry.ITEM.getId(ArmorStandChances.get().getRareChestplate(random)).toString()
                : Registry.ITEM.getId(ArmorStandChances.get().getCommonChestplate(random)).toString();
            ((NbtCompound)newNBT.getList("ArmorItems", 10).get(2)).putString("id", chesplateString);
            ((NbtCompound)newNBT.getList("ArmorItems", 10).get(2)).putByte("Count", (byte) 1);
            NbtCompound chestplateTagNBT = new NbtCompound();
            chestplateTagNBT.putInt("Damage", 0);
            ((NbtCompound)newNBT.getList("ArmorItems", 10).get(2)).put("tag", chestplateTagNBT);

            // Helmet
            String helmetString = isRare
                ? Registry.ITEM.getId(ArmorStandChances.get().getRareHelmet(random)).toString()
                : Registry.ITEM.getId(ArmorStandChances.get().getCommonHelmet(random)).toString();
            ((NbtCompound)newNBT.getList("ArmorItems", 10).get(3)).putString("id", helmetString);
            ((NbtCompound)newNBT.getList("ArmorItems", 10).get(3)).putByte("Count", (byte) 1);
            NbtCompound helmetTagNBT = new NbtCompound();
            helmetTagNBT.putInt("Damage", 0);
            ((NbtCompound)newNBT.getList("ArmorItems", 10).get(3)).put("tag", helmetTagNBT);
            globalEntityInfo = new Structure.StructureEntityInfo(globalEntityInfo.pos, globalEntityInfo.blockPos, newNBT);
        }
        return globalEntityInfo;
    }

    @Nullable
    @Override
    public Structure.StructureBlockInfo process(WorldView worldView, BlockPos pos, BlockPos blockPos, Structure.StructureBlockInfo blockInfoLocal, Structure.StructureBlockInfo blockInfoGlobal, StructurePlacementData structurePlacementData) {
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return BSModProcessors.ARMORSTAND_PROCESSOR;
    }
}
