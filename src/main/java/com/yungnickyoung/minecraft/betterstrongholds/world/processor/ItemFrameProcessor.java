package com.yungnickyoung.minecraft.betterstrongholds.world.processor;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholds;
import com.yungnickyoung.minecraft.betterstrongholds.init.BSModProcessors;
import com.yungnickyoung.minecraft.betterstrongholds.world.ItemFrameChances;
import com.yungnickyoung.minecraft.betterstrongholds.world.jigsaw.StructureEntityProcessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

/**
 * Fills item frames with a random item.
 * The type of random item depends on the item already in the frame.
 */
public class ItemFrameProcessor extends StructureEntityProcessor {
    public static final ItemFrameProcessor INSTANCE = new ItemFrameProcessor();
    public static final Codec<ItemFrameProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public Structure.StructureEntityInfo processEntity(WorldView worldView,
                                                       BlockPos structurePiecePos,
                                                       BlockPos structurePieceBottomCenterPos,
                                                       Structure.StructureEntityInfo localEntityInfo,
                                                       Structure.StructureEntityInfo globalEntityInfo,
                                                       StructurePlacementData structurePlacementData
    ) {
        if (globalEntityInfo.tag.getString("id").equals("minecraft:item_frame")) {
            Random random = structurePlacementData.getRandom(globalEntityInfo.blockPos);

            // Determine which pool we are grabbing from
            String item;
            try {
                item = globalEntityInfo.tag.getCompound("Item").get("id").toString();
            } catch (Exception e) {
                BetterStrongholds.LOGGER.info("Unable to randomize item frame at {}", globalEntityInfo.blockPos);
                return globalEntityInfo;
            }

            // Set the item in the item frame's NBT
            CompoundTag newNBT = globalEntityInfo.tag.copy();
            if (item.equals("\"minecraft:iron_sword\"")) { // Armoury pool
                String randomItemString = Registry.ITEM.getId(ItemFrameChances.get().getArmouryItem(random)).toString();
                if (!randomItemString.equals("minecraft:air")) {
                    newNBT.getCompound("Item").putString("id", randomItemString);
                }
            } else if (item.equals("\"minecraft:bread\"")) { // Storage pool
                String randomItemString = Registry.ITEM.getId(ItemFrameChances.get().getStorageItem(random)).toString();
                if (!randomItemString.equals("minecraft:air")) {
                    newNBT.getCompound("Item").putString("id", randomItemString);
                }
            }

            // Randomize rotation
            int randomRotation = random.nextInt(8);
            newNBT.putByte("ItemRotation", (byte) randomRotation);

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
        return BSModProcessors.ITEMFRAME_PROCESSOR;
    }
}
