//package com.yungnickyoung.minecraft.betterstrongholds.mixin;
//
//import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholds;
//import net.minecraft.structure.Structure;
//import net.minecraft.util.BlockMirror;
//import net.minecraft.util.BlockRotation;
//import net.minecraft.util.math.BlockBox;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.ServerWorldAccess;
//import org.jetbrains.annotations.Nullable;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.ModifyVariable;
//
//@Mixin(Structure.class)
//public class EntityProcessorMixin {
//    @ModifyVariable(
//        method = "use",
//        at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/List;iterator()Ljava/util/Iterator;")
//    )
//    private void spawnEntities(ServerWorldAccess serverWorldAccess, BlockPos pos, BlockMirror blockMirror, BlockRotation blockRotation, BlockPos pivot, @Nullable BlockBox area, boolean bl) {
//        BetterStrongholds.LOGGER.info("TEST123");
//    }
//}
