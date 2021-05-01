//package com.yungnickyoung.minecraft.betterstrongholds.world.processor;
//
//import com.mojang.serialization.Codec;
//import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholds;
//import com.yungnickyoung.minecraft.betterstrongholds.init.BSModProcessors;
//import com.yungnickyoung.minecraft.betterstrongholds.world.ArmorStandChances;
//import net.minecraft.structure.processor.StructureProcessor;
//
//import java.util.Random;
//
///**
// * Gives armor stands random armor depending on the type of armor
// * they are already wearing.
// */
//public class ArmorStandProcessor extends StructureProcessor {
//    public static final ArmorStandProcessor INSTANCE = new ArmorStandProcessor();
//    public static final Codec<ArmorStandProcessor> CODEC = Codec.unit(() -> INSTANCE);
//
//    @Override
//    public Template.EntityInfo processEntity(IWorldReader world, BlockPos seedPos, Template.EntityInfo localEntityInfo, Template.EntityInfo globalEntityInfo, PlacementSettings placementSettings, Template template) {
//        if (globalEntityInfo.nbt.getString("id").equals("minecraft:armor_stand")) {
//            ListNBT armorItems = globalEntityInfo.nbt.getList("ArmorItems", 10);
//            Random random = placementSettings.getRandom(globalEntityInfo.blockPos);
//
//            // Type depends on the helmet
//            String helmet;
//            try {
//                helmet = ((CompoundNBT) armorItems.get(3)).get("id").toString();
//            } catch(Exception e) {
//                BetterStrongholds.LOGGER.info("Unable to randmize armor stand at {}. Missing helmet?", globalEntityInfo.blockPos);
//                return globalEntityInfo;
//            }
//
//            boolean isRare = false;
//            if (helmet.equals("\"minecraft:diamond_helmet\""))
//                isRare = true;
//
//            CompoundNBT newNBT = globalEntityInfo.nbt.copy();
//            // Boots
//            String bootsString = isRare
//                ? ArmorStandChances.get().getRareBoots(random).getRegistryName().toString()
//                : ArmorStandChances.get().getCommonBoots(random).getRegistryName().toString();
//            ((CompoundNBT)newNBT.getList("ArmorItems", 10).get(0)).putString("id", bootsString);
//            ((CompoundNBT)newNBT.getList("ArmorItems", 10).get(0)).putByte("Count", (byte) 1);
//            CompoundNBT bootsTagNBT = new CompoundNBT();
//            bootsTagNBT.putInt("Damage", 0);
//            ((CompoundNBT)newNBT.getList("ArmorItems", 10).get(0)).put("tag", bootsTagNBT);
//
//            // Leggings
//            String leggingsString = isRare
//                ? ArmorStandChances.get().getRareLeggings(random).getRegistryName().toString()
//                : ArmorStandChances.get().getCommonLeggings(random).getRegistryName().toString();
//            ((CompoundNBT)newNBT.getList("ArmorItems", 10).get(1)).putString("id", leggingsString);
//            ((CompoundNBT)newNBT.getList("ArmorItems", 10).get(1)).putByte("Count", (byte) 1);
//            CompoundNBT leggingsTagNBT = new CompoundNBT();
//            leggingsTagNBT.putInt("Damage", 0);
//            ((CompoundNBT)newNBT.getList("ArmorItems", 10).get(1)).put("tag", leggingsTagNBT);
//
//            // Chestplate
//            String chesplateString = isRare
//                ? ArmorStandChances.get().getRareChestplate(random).getRegistryName().toString()
//                : ArmorStandChances.get().getCommonChestplate(random).getRegistryName().toString();
//            ((CompoundNBT)newNBT.getList("ArmorItems", 10).get(2)).putString("id", chesplateString);
//            ((CompoundNBT)newNBT.getList("ArmorItems", 10).get(2)).putByte("Count", (byte) 1);
//            CompoundNBT chestplateTagNBT = new CompoundNBT();
//            chestplateTagNBT.putInt("Damage", 0);
//            ((CompoundNBT)newNBT.getList("ArmorItems", 10).get(2)).put("tag", chestplateTagNBT);
//
//            // Helmet
//            String helmetString = isRare
//                ? ArmorStandChances.get().getRareHelmet(random).getRegistryName().toString()
//                : ArmorStandChances.get().getCommonHelmet(random).getRegistryName().toString();
//            ((CompoundNBT)newNBT.getList("ArmorItems", 10).get(3)).putString("id", helmetString);
//            ((CompoundNBT)newNBT.getList("ArmorItems", 10).get(3)).putByte("Count", (byte) 1);
//            CompoundNBT helmetTagNBT = new CompoundNBT();
//            helmetTagNBT.putInt("Damage", 0);
//            ((CompoundNBT)newNBT.getList("ArmorItems", 10).get(3)).put("tag", helmetTagNBT);
//            globalEntityInfo = new Template.EntityInfo(globalEntityInfo.pos, globalEntityInfo.blockPos, newNBT);
//        }
//        return globalEntityInfo;
//    }
//
//    protected IStructureProcessorType<?> getType() {
//        return BSModProcessors.ARMORSTAND_PROCESSOR;
//    }
//}
