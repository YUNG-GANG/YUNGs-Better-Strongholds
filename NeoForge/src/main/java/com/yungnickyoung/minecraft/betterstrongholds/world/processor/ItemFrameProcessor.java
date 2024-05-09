package com.yungnickyoung.minecraft.betterstrongholds.world.processor;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholdsCommon;
import com.yungnickyoung.minecraft.betterstrongholds.module.StructureProcessorTypeModule;
import com.yungnickyoung.minecraft.betterstrongholds.world.ItemFrameChances;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Fills item frames with a random item.
 * The type of random item depends on the item already in the frame.
 */
@ParametersAreNonnullByDefault
public class ItemFrameProcessor extends StructureProcessor {
    public static final ItemFrameProcessor INSTANCE = new ItemFrameProcessor();
    public static final Codec<StructureProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public StructureTemplate.StructureEntityInfo processEntity(LevelReader levelReader,
                                                               BlockPos structurePiecePos,
                                                               StructureTemplate.StructureEntityInfo localEntityInfo,
                                                               StructureTemplate.StructureEntityInfo globalEntityInfo,
                                                               StructurePlaceSettings structurePlaceSettings,
                                                               StructureTemplate template) {
        if (globalEntityInfo.nbt.getString("id").equals("minecraft:item_frame")) {
            RandomSource random = structurePlaceSettings.getRandom(globalEntityInfo.blockPos);

            // Determine which pool we are grabbing from
            String item;
            try {
                item = globalEntityInfo.nbt.getCompound("Item").get("id").toString();
            } catch (Exception e) {
                BetterStrongholdsCommon.LOGGER.info("Unable to randomize item frame at {}", globalEntityInfo.blockPos);
                return globalEntityInfo;
            }

            // Set the item in the item frame's NBT
            CompoundTag newNBT = globalEntityInfo.nbt.copy();
            if (item.equals("\"minecraft:iron_sword\"")) { // Armoury pool
                String randomItemString = BuiltInRegistries.ITEM.getKey(ItemFrameChances.get().getArmouryItem(random)).toString();
                if (!randomItemString.equals("minecraft:air")) {
                    newNBT.getCompound("Item").putString("id", randomItemString);
                }
            } else if (item.equals("\"minecraft:bread\"")) { // Storage pool
                String randomItemString = BuiltInRegistries.ITEM.getKey(ItemFrameChances.get().getStorageItem(random)).toString();
                if (!randomItemString.equals("minecraft:air")) {
                    newNBT.getCompound("Item").putString("id", randomItemString);
                }
            }

            // Required to suppress dumb log spam
            newNBT.putInt("TileX", globalEntityInfo.blockPos.getX());
            newNBT.putInt("TileY", globalEntityInfo.blockPos.getY());
            newNBT.putInt("TileZ", globalEntityInfo.blockPos.getZ());

            // Randomize rotation
            int randomRotation = random.nextInt(8);
            newNBT.putByte("ItemRotation", (byte) randomRotation);

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
        return StructureProcessorTypeModule.ITEMFRAME_PROCESSOR;
    }
}
