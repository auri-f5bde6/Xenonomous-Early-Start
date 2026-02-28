package com.github.auri_f5bde6.xeno_early_start.mixins;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.text.Style;
import net.minecraft.text.TextVisitFactory;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TextVisitFactory.class)
public class TextVisitFactoryMixin {

    @Definition(id = "formatting", local = @Local(type = Formatting.class, name = "formatting"))
    @Expression("formatting!=null")
    @WrapOperation(method = "visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z", at = @At("MIXINEXTRAS:EXPRESSION"))
    private static boolean allowP(Object left, Object right, Operation<Boolean> original, @Local(name = "d") char colourCode) {
        return original.call(left, right) || colourCode == 'p';
    }

    @WrapOperation(method = "visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/Style;withExclusiveFormatting(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/Style;"))
    private static Style replaceWithPink(Style instance, Formatting textColor, Operation<Style> original, @Local(name = "d") char colourCode) {
        if (colourCode == 'p') {
            return instance.withColor(0xf5bde6);
        }
        return original.call(instance, textColor);
    }

}
