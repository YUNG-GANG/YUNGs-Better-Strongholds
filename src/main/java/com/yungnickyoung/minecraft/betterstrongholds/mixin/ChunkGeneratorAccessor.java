package com.yungnickyoung.minecraft.betterstrongholds.mixin;

import com.mojang.serialization.Codec;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ChunkGenerator.class)
public interface ChunkGeneratorAccessor {
    @Accessor("settings")
    void betterstrongholds_setSettings(DimensionStructuresSettings dimensionStructuresSettings);

    @Invoker("func_230347_a_")
    Codec<ChunkGenerator> betterstrongholds_getCodec();
}
