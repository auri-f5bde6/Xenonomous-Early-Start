package com.github.auri_f5bde6.xeno_early_start.mixins.mob_changes;

import com.github.auri_f5bde6.xeno_early_start.ProgressionModConfig;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @WrapMethod(method = "onPlayerCollision")
    public void batApplyNausea(PlayerEntity player, Operation<Void> original) {
        if ((Entity) (Object) this instanceof BatEntity batEntity && ProgressionModConfig.config.mobChanges.getBatGivesPlayerNausea()) {
            // 10 second, 20 tick per second
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 20 * 10), batEntity);
        }
        original.call(player);
    }
}