//package com.yungnickyoung.minecraft.betterstrongholds.world.processor;
//
//import com.mojang.serialization.Codec;
//import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholds;
//import com.yungnickyoung.minecraft.betterstrongholds.init.BSModProcessors;
//import com.yungnickyoung.minecraft.betterstrongholds.world.ItemFrameChances;
//import mcp.MethodsReturnNonnullByDefault;
//import net.minecraft.nbt.CompoundNBT;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.IWorldReader;
//import net.minecraft.world.gen.feature.template.IStructureProcessorType;
//import net.minecraft.world.gen.feature.template.PlacementSettings;
//import net.minecraft.world.gen.feature.template.StructureProcessor;
//import net.minecraft.world.gen.feature.template.Template;
//
//import javax.annotation.ParametersAreNonnullByDefault;
//import java.util.Random;
//
///**
// * Fills item frames with a random item.
// * The type of random item depends on the item already in the frame.
// */
//@MethodsReturnNonnullByDefault
//public class ItemFrameProcessor extends StructureProcessor {
//    public static final ItemFrameProcessor INSTANCE = new ItemFrameProcessor();
//    public static final Codec<ItemFrameProcessor> CODEC = Codec.unit(() -> INSTANCE);
//
//    @Override
//    @ParametersAreNonnullByDefault
//    public Template.EntityInfo processEntity(IWorldReader world, BlockPos seedPos, Template.EntityInfo localEntityInfo, Template.EntityInfo globalEntityInfo, PlacementSettings placementSettings, Template template) {
//        if (globalEntityInfo.nbt.getString("id").equals("minecraft:item_frame")) {
//            Random random = placementSettings.getRandom(globalEntityInfo.blockPos);
//
//            // Determine which pool we are grabbing from
//            String item;
//            try {
//                item = globalEntityInfo.nbt.getCompound("Item").get("id").toString();
//            } catch (Exception e) {
//                BetterStrongholds.LOGGER.info("Unable to randmize item frame at {}", globalEntityInfo.blockPos);
//                return globalEntityInfo;
//            }
//
//            // Set the item in the item frame's NBT
//            CompoundNBT newNBT = globalEntityInfo.nbt.copy();
//            if (item.equals("\"minecraft:iron_sword\"")) { // Armoury pool
//                String randomItemString = ItemFrameChances.get().getArmouryItem(random).getRegistryName().toString();
//                if (!randomItemString.equals("minecraft:air")) {
//                    newNBT.getCompound("Item").putString("id", randomItemString);
//                }
//            } else if (item.equals("\"minecraft:bread\"")) { // Storage pool
//                String randomItemString = ItemFrameChances.get().getStorageItem(random).getRegistryName().toString();
//                if (!randomItemString.equals("minecraft:air")) {
//                    newNBT.getCompound("Item").putString("id", randomItemString);
//                }            }
//
//            // Randomize rotation
//            int randomRotation = random.nextInt(8);
//            newNBT.putByte("ItemRotation", (byte) randomRotation);
//
//            globalEntityInfo = new Template.EntityInfo(globalEntityInfo.pos, globalEntityInfo.blockPos, newNBT);
//        }
//        return globalEntityInfo;
//    }
//
//    protected IStructureProcessorType<?> getType() {
//        return BSModProcessors.ITEMFRAME_PROCESSOR;
//    }
//}
