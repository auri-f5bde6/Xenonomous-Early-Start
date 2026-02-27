package com.github.auri_f5bde6.xeno_early_start.mixins;

import com.github.auri_f5bde6.xeno_early_start.advancements.AdvancementSorting;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mixin(AdvancementManager.class)
public abstract class AdvancementManagerMixin {
    @Mutable
    @Shadow
    @Final
    private Set<Advancement> roots;

    @WrapMethod(method = "remove")
    void remove(Advancement advancement, Operation<Void> original) {
        original.call(advancement);
        AdvancementSorting.remove(advancement);
    }

    @Inject(method = "setListener", at = @At(value = "HEAD"))
    void sort(AdvancementManager.Listener listener, CallbackInfo ci) {
        roots = roots.stream().sorted(AdvancementSorting::shouldBeAfter).collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
