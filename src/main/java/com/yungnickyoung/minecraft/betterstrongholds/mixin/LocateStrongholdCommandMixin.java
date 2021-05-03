package com.yungnickyoung.minecraft.betterstrongholds.mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.server.command.LocateCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.gen.feature.StructureFeature;
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
            new TranslatableText(
                "betterstrongholds.commands.locate.stronghold"
            )
        );

    @Inject(method = "execute", at = @At(value = "HEAD"), cancellable = true)
    private static void overrideLocateVanillaStronghold(ServerCommandSource cmdSource,
                                                        StructureFeature<?> structure,
                                                        CallbackInfoReturnable<Integer> ci) throws CommandSyntaxException {
        if (structure == StructureFeature.STRONGHOLD) {
            throw OLD_STRONGHOLD_EXCEPTION.create();
        }
    }
}
