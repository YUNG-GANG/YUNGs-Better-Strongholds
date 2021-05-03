package com.yungnickyoung.minecraft.betterstrongholds.mixin;

import com.yungnickyoung.minecraft.betterstrongholds.world.jigsaw.StructureEntityProcessor;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mixin(Structure.class)
public class EntityProcessorMixin {
    @Shadow
    private List<Structure.StructureEntityInfo> entities;

    @Inject(
        method = "Lnet/minecraft/structure/Structure;place(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/structure/StructurePlacementData;Ljava/util/Random;I)Z",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/structure/Structure;spawnEntities(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/BlockMirror;Lnet/minecraft/util/BlockRotation;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockBox;Z)V")
    )
    private void processEntities(ServerWorldAccess serverWorldAccess, BlockPos structurePiecePos, BlockPos structurePieceBottomCenterPos, StructurePlacementData placementData, Random random, int i, CallbackInfoReturnable<Boolean> cir) {
        List<Structure.StructureEntityInfo> processedEntities = new ArrayList<>();

        for (Structure.StructureEntityInfo rawEntityInfo : this.entities) {
            // Calculate transformed position so processors have access to the actual global world coordinates of the entity
            Vec3d globalPos = Structure.transformAround(rawEntityInfo.pos, placementData.getMirror(), placementData.getRotation(), placementData.getPosition()).add(Vec3d.of(structurePiecePos));
            BlockPos globalBlockPos = Structure.transformAround(rawEntityInfo.blockPos, placementData.getMirror(), placementData.getRotation(), placementData.getPosition()).add(structurePiecePos);
            Structure.StructureEntityInfo globalEntityInfo = new Structure.StructureEntityInfo(globalPos, globalBlockPos, rawEntityInfo.tag);

            // Apply processors
            for (StructureProcessor processor : placementData.getProcessors()) {
                if (processor instanceof StructureEntityProcessor) {
                    globalEntityInfo = ((StructureEntityProcessor) processor).processEntity(serverWorldAccess, structurePiecePos, structurePieceBottomCenterPos, rawEntityInfo, globalEntityInfo, placementData);
                    if (globalEntityInfo == null) break;
                }
            }

            if (globalEntityInfo != null) {
                // Reset the position to be local to the structure piece since vanilla recalculates the transformed position
                globalEntityInfo = new Structure.StructureEntityInfo(rawEntityInfo.pos, rawEntityInfo.blockPos, globalEntityInfo.tag);
                processedEntities.add(globalEntityInfo);
            }
        }

        this.entities = processedEntities;
    }
}
