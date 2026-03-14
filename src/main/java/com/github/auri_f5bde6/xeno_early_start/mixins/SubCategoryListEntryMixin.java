package com.github.auri_f5bde6.xeno_early_start.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.gui.entries.SubCategoryListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SubCategoryListEntry.class)
public class SubCategoryListEntryMixin {
    @WrapOperation(method = "isRequiresRestart", at = @At(value = "INVOKE", target = "Lme/shedaniel/clothconfig2/api/AbstractConfigListEntry;isRequiresRestart()Z"))
    boolean fixWeirdQuirk(AbstractConfigListEntry instance, Operation<Boolean> original) {
        return original.call(instance) && instance.isEdited();
    }
}
