package com.github.auri_f5bde6.xeno_early_start.mixins;

import com.github.auri_f5bde6.xeno_early_start.text.XenoEarlyStartTextType;
import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Text.Serializer.class)
public abstract class TextSerializerMixin {
    @Definition(id = "jsonObject", local = @Local(type = JsonObject.class, name = "jsonObject"))
    @Definition(id = "has", method = "Lcom/google/gson/JsonObject;has(Ljava/lang/String;)Z")
    @Expression("jsonObject.has('keybind')")
    @WrapOperation(method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/text/MutableText;", at = @At("MIXINEXTRAS:EXPRESSION"))
    boolean allowCustomField(JsonObject instance, String memberName, Operation<Boolean> original) {
        return original.call(instance, memberName) || instance.has("xeno_early_start");
    }

    @WrapOperation(method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/text/MutableText;", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/Text;keybind(Ljava/lang/String;)Lnet/minecraft/text/MutableText;"))
    MutableText replaceText(String string, Operation<MutableText> original, @Local(name = "jsonObject") JsonObject jsonObject) {
        if (jsonObject.has("xeno_early_start")) {
            return XenoEarlyStartTextType.getText(string);
        } else {
            return original.call(string);
        }
    }

    @Definition(id = "getString", method = "Lnet/minecraft/util/JsonHelper;getString(Lcom/google/gson/JsonObject;Ljava/lang/String;)Ljava/lang/String;")
    @Expression("getString(?,'keybind')")
    @WrapOperation(method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/text/MutableText;", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
    String maybeReturnCustomValue(JsonObject object, String element, Operation<String> original, @Local(name = "jsonObject") JsonObject jsonObject) {
        if (jsonObject.has("xeno_early_start")) {
            return JsonHelper.getString(object, "xeno_early_start");
        } else {
            return original.call(object, element);
        }
    }
}
