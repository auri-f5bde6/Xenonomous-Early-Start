package net.hellomouse.xeno_early_start.mixins;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Predicate;

@Mixin(PolarBearEntity.class)
public abstract class PolarBearEntityMixin extends AnimalEntity implements Angerable {
    protected PolarBearEntityMixin(EntityType<? extends AnimalEntity> arg, World arg2) {
        super(arg, arg2);
    }

    @Definition(id = "shouldAngerAt", method = "Lnet/minecraft/entity/mob/Angerable;shouldAngerAt(Lnet/minecraft/entity/LivingEntity;)Z")
    @Expression("this::shouldAngerAt")
    @ModifyExpressionValue(method = "initGoals", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
    Predicate<LivingEntity> initGoals(Predicate<LivingEntity> original) {
        return livingEntity -> {
            if (!PolarBearEntityMixin.super.canTarget(livingEntity)) {
                return false;
            } else {
                return livingEntity.getType() == EntityType.PLAYER;
            }
        };
    }
}