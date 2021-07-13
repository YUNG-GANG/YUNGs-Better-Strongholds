package com.yungnickyoung.minecraft.betterstrongholds.init;

import com.yungnickyoung.minecraft.betterstrongholds.BetterStrongholds;
import com.yungnickyoung.minecraft.betterstrongholds.criteria.SafeStructurePositionTrigger;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class BSModCriterions {
    public static final SafeStructurePositionTrigger SAFE_STRUCTURE_POSITION_TRIGGER = new SafeStructurePositionTrigger(new ResourceLocation(BetterStrongholds.MOD_ID, "structure_location"));

    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(BSModCriterions::commonSetup);
        MinecraftForge.EVENT_BUS.addListener(SafeStructurePositionTrigger::playerTick);
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            CriteriaTriggers.register(SAFE_STRUCTURE_POSITION_TRIGGER);
        });
    }
}
