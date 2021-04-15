/*
 * 100% credit to TelepathicGrunt for this code.
 * See the original code from RepurposedStructures here:
 * https://github.com/TelepathicGrunt/RepurposedStructures/blob/1.16/src/main/java/com/telepathicgrunt/repurposedstructures/mixin/EnderEyeStrongholdLocatingMixin.java
 */

package com.yungnickyoung.minecraft.betterstrongholds.mixin;

import com.yungnickyoung.minecraft.betterstrongholds.init.ModStructures;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnderEyeItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EnderEyeItem.class)
public class EnderEyeStrongholdLocatingMixin {
    @ModifyVariable(
        method = "onItemRightClick",
        at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/gen/ChunkGenerator;func_235956_a_(Lnet/minecraft/world/server/ServerWorld;Lnet/minecraft/world/gen/feature/structure/Structure;Lnet/minecraft/util/math/BlockPos;IZ)Lnet/minecraft/util/math/BlockPos;")
    )
    private BlockPos locateBetterStronghold(BlockPos blockPos, World world, PlayerEntity player) {
        return returnClosestStronghold(blockPos, (ServerWorld)world, player.getPosition());
    }

    private static BlockPos returnClosestStronghold(BlockPos blockPos, ServerWorld world, BlockPos playerPos) {
        ChunkGenerator chunkGenerator = world.getChunkProvider().getChunkGenerator();
        BlockPos closestPos = returnCloserPos(blockPos, chunkGenerator.func_235956_a_(world, ModStructures.BETTER_STRONGHOLD.get(), playerPos, 100, false), playerPos);
        closestPos = returnCloserPos(closestPos, chunkGenerator.func_235956_a_(world, ModStructures.BETTER_STRONGHOLD.get(), playerPos, 100, false), playerPos);
        return closestPos;
    }

    private static BlockPos returnCloserPos(BlockPos blockPos1, BlockPos blockPos2, BlockPos centerPos) {
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
