package com.yungnickyoung.minecraft.betterstrongholds.mixin;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Set;

/**
 * Makes it so vanilla stronghold positions are not locatable via /locate and ender eyes.
 */
@Mixin(ChunkGenerator.class)
public class CancelSearchVanillaStrongholdMixinFabric {
    @Inject(method = "getNearestGeneratedStructure(Ljava/util/Set;Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/world/level/StructureFeatureManager;IIIZJLnet/minecraft/world/level/levelgen/structure/placement/RandomSpreadStructurePlacement;)Lcom/mojang/datafixers/util/Pair;",
            at = @At(value = "HEAD"),
            cancellable = true)
    private static void cancelSearchVanillaStronghold(Set<Holder<ConfiguredStructureFeature<?, ?>>> configuredStructureFeatureHolderSet,
                                                      LevelReader levelReader,
                                                      StructureFeatureManager structureFeatureManager,
                                                      int p_208063_,
                                                      int p_208064_,
                                                      int p_208065_,
                                                      boolean p_208066_,
                                                      long p_208067_,
                                                      RandomSpreadStructurePlacement p_208068_,
                                                      CallbackInfoReturnable<Pair<BlockPos, Holder<ConfiguredStructureFeature<?, ?>>>> cir) {
        for (Holder<ConfiguredStructureFeature<?, ?>> configuredStructureFeatureHolder : configuredStructureFeatureHolderSet) {
            if (Registry.STRUCTURE_FEATURE.getKey(configuredStructureFeatureHolder.value().feature).equals(new ResourceLocation("stronghold"))) {
                cir.setReturnValue(null);
            }
        }
    }

    @Inject(method = "getPlacementsForFeature",
            at = @At(value = "HEAD"),
            cancellable = true)
    private void cancelSearchVanillaStronghold(Holder<ConfiguredStructureFeature<?, ?>> configuredStructureFeatureHolder,
                                               CallbackInfoReturnable<List<StructurePlacement>> cir) {
        if (Registry.STRUCTURE_FEATURE.getKey(configuredStructureFeatureHolder.value().feature).equals(new ResourceLocation("stronghold"))) {
            cir.setReturnValue(Lists.newArrayList());
        }
    }
}
