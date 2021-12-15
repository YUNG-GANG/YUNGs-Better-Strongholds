package com.yungnickyoung.minecraft.betterstrongholds.mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.commands.LocateCommand;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Overrides behavior of /locate stronghold.
 */
@Mixin(LocateCommand.class)
public class LocateStrongholdCommandMixin {
    private static final SimpleCommandExceptionType OLD_STRONGHOLD_EXCEPTION =
        new SimpleCommandExceptionType(
            new TranslatableComponent(
                "betterstrongholds.commands.locate.stronghold"
            )
        );

    @Inject(method = "locate", at = @At(value = "HEAD"), cancellable = true)
    private static void overrideLocateVanillaStronghold(CommandSourceStack cmdSource,
                                                        StructureFeature<?> structure,
                                                        CallbackInfoReturnable<Integer> ci) throws CommandSyntaxException {
        if (structure == StructureFeature.STRONGHOLD) {
            throw OLD_STRONGHOLD_EXCEPTION.create();
        }
    }
}
