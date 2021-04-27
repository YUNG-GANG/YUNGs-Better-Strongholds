package com.yungnickyoung.minecraft.betterstrongholds.mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.impl.LocateCommand;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.gen.feature.structure.Structure;
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
            new TranslationTextComponent(
                "betterstrongholds.commands.locate.stronghold"
            )
        );

    @Inject(method = "func_241053_a_", at = @At(value = "HEAD"), cancellable = true)
    private static void overrideLocateVanillaStronghold(CommandSource cmdSource,
                                                Structure<?> structure,
                                                CallbackInfoReturnable<Integer> ci) throws CommandSyntaxException {
        if (structure == Structure.STRONGHOLD) {
            throw OLD_STRONGHOLD_EXCEPTION.create();
        }
    }
}
