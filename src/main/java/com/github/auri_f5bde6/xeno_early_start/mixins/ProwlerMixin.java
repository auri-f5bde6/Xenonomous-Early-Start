package com.github.auri_f5bde6.xeno_early_start.mixins;

import com.github.auri_f5bde6.xeno_early_start.CoalDust;
import com.github.auri_f5bde6.xeno_early_start.entity.ProwlerEntity;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.mob.CreeperEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(CreeperEntity.class)
public class ProwlerMixin {
    @Shadow
    private int explosionRadius;

    @WrapMethod(method = "explode")
    private void explode(Operation<Void> original) {
        original.call();
        if ((CreeperEntity) (Object) this instanceof ProwlerEntity e && !e.getWorld().isClient) {
            CoalDust.INSTANCE.applyStatusEffect(e.getWorld(), e.getBlockPos(), explosionRadius + 3, List.of(e.getBlockPos()), false);
        }
    }
}
