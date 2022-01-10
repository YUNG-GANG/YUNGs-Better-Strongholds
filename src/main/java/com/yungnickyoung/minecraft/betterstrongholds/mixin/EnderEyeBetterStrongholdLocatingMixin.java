/*
 * 100% credit to TelepathicGrunt for this code.
 * See the original code from RepurposedStructures here:
 * https://github.com/TelepathicGrunt/RepurposedStructures/blob/1.16/src/main/java/com/telepathicgrunt/repurposedstructures/mixin/EnderEyeStrongholdLocatingMixin.java
 */

package com.yungnickyoung.minecraft.betterstrongholds.mixin;

import com.yungnickyoung.minecraft.betterstrongholds.init.BSModStructureFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnderEyeItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EnderEyeItem.class)
public class EnderEyeBetterStrongholdLocatingMixin {
    @ModifyVariable(
        method = "use",
        at = @At(value = "INVOKE_ASSIGN",
                target = "Lnet/minecraft/world/level/chunk/ChunkGenerator;findNearestMapFeature(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/levelgen/feature/StructureFeature;Lnet/minecraft/core/BlockPos;IZ)Lnet/minecraft/core/BlockPos;"))
    private BlockPos locateBetterStronghold(BlockPos blockPos, Level level, Player player) {
        return locateClosestBetterStronghold(blockPos, (ServerLevel)level, player.blockPosition());
    }

    private static BlockPos locateClosestBetterStronghold(BlockPos blockPos, ServerLevel level, BlockPos playerPos) {
        ChunkGenerator chunkGenerator = level.getChunkSource().getGenerator();
        BlockPos closestPos = returnClosestPosition(blockPos, chunkGenerator.findNearestMapFeature(level, BSModStructureFeatures.BETTER_STRONGHOLD.get(), playerPos, 100, false), playerPos);
        closestPos = returnClosestPosition(closestPos, chunkGenerator.findNearestMapFeature(level, BSModStructureFeatures.BETTER_STRONGHOLD.get(), playerPos, 100, false), playerPos);
        return closestPos;
    }

    private static BlockPos returnClosestPosition(BlockPos blockPos1, BlockPos blockPos2, BlockPos centerPos) {
        if (blockPos1 == null && blockPos2 == null) {
            return null;
        }
        else if (blockPos1 == null) {
            return blockPos2;
        }
        else if (blockPos2 == null) {
            return blockPos1;
        }

        double distance1 = Math.pow(blockPos1.getX() - centerPos.getX(), 2) + Math.pow(blockPos1.getZ() - centerPos.getZ(), 2);
        double distance2 = Math.pow(blockPos2.getX() - centerPos.getX(), 2) + Math.pow(blockPos2.getZ() - centerPos.getZ(), 2);
        if (distance1 < distance2) {
            return blockPos1;
        } else {
            return blockPos2;
        }
    }
}
