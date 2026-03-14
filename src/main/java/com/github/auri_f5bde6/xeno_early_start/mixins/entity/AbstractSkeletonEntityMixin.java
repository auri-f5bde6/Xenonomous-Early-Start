package com.github.auri_f5bde6.xeno_early_start.mixins.entity;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractSkeletonEntity.class)
public class AbstractSkeletonEntityMixin {
    @Definition(id = "spawnEntity", method = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z")
    @Expression("?.spawnEntity(@(?))")
    @ModifyExpressionValue(method = "attack", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
    PersistentProjectileEntity maybeSetOnFire(PersistentProjectileEntity original) {
        if (((AbstractSkeletonEntity) (Object) this).isOnFire()) {
            original.setOnFire(true);
        }
        return original;
    }
}
