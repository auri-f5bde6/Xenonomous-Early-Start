package com.github.auri_f5bde6.xeno_early_start.mixins;

import com.github.auri_f5bde6.xeno_early_start.text.XenoEarlyStartTextContent;
import com.github.auri_f5bde6.xeno_early_start.text.XenoEarlyStartTextType;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.text.MutableText;
import net.minecraft.text.TextContent;
import net.minecraft.text.TranslatableTextContent;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MutableText.class)
public class MutableTextMixin {
    @WrapMethod(method = "of")
    private static MutableText of(TextContent content, Operation<MutableText> original) {
        if (content instanceof TranslatableTextContent c && !(content instanceof XenoEarlyStartTextContent)) {
            var key = c.getKey();
            var customText = XenoEarlyStartTextType.getText(key);
            if (customText != null) {
                return customText;
            }
        }
        return original.call(content);
    }
}
