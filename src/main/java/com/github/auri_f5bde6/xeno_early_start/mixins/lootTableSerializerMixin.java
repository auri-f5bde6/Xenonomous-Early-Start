package com.github.auri_f5bde6.xeno_early_start.mixins;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.loot.LootTable;
import net.minecraft.util.JsonHelper;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Type;

@Mixin(LootTable.Serializer.class)
public class lootTableSerializerMixin {
    @Inject(method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/loot/LootTable;", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/JsonHelper;deserialize(Lcom/google/gson/JsonObject;Ljava/lang/String;Ljava/lang/Object;Lcom/google/gson/JsonDeserializationContext;Ljava/lang/Class;)Ljava/lang/Object;"), cancellable = true)
    void deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext, CallbackInfoReturnable<LootTable> cir, @Local JsonObject jsonObject) {
        if (jsonObject.has("xeno_early_start:conditions")) {
            var conditions = JsonHelper.getArray(jsonObject, "xeno_early_start:conditions");
            for (JsonElement element : conditions.asList()) {
                if (!CraftingHelper.getCondition(element.getAsJsonObject()).test(ICondition.IContext.EMPTY)) {
                    cir.setReturnValue(LootTable.EMPTY);
                }
            }
        }
    }
}
