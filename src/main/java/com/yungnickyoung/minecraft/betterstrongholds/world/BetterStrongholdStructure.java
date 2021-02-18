package com.yungnickyoung.minecraft.betterstrongholds.world;

import com.mojang.serialization.Codec;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.StrongholdStructure;

public class BetterStrongholdStructure extends StrongholdStructure {

    public BetterStrongholdStructure(Codec<NoFeatureConfig> p_i231996_1_) {
        super(p_i231996_1_);
    }

    @Override
    public GenerationStage.Decoration getDecorationStage() {
        return GenerationStage.Decoration.STRONGHOLDS;
    }


    /**
     * Returns the name displayed when the locate command is used.
     * I believe (not 100% sure) that the lowercase form of this value must also match
     * the key of the entry added to Structure.field_236365_a_ during common setup.
     *
     * See {@link com.yungnickyoung.minecraft.bettermineshafts.init.ModStructures#commonSetup}
     */
    @Override
    public String getStructureName() {
        return "Better Stronghold";
    }
}
