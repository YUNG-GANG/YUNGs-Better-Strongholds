package com.yungnickyoung.minecraft.betterstrongholds.mixin;

import net.minecraft.world.level.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Strongholds are unique structures in that removing them from biomes
 * is not sufficient to prevent them from spawning.This mixin is an alternative approach to this
 * which should be compatible with both regular and JSON dimensions.
 */
@Mixin(ChunkGenerator.class)
public class VanillaStrongholdSpawnMixin {
    @Inject(method = "generateStrongholds", at = @At(value = "HEAD"), cancellable = true)
    private void removeVanillaStronghold(CallbackInfo ci) {
        ci.cancel();
    }
}
