package net.hellomouse.xeno_early_start.mixins.mob_changes;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.hellomouse.xeno_early_start.ProgressionModConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
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

    @WrapMethod(method = "createPolarBearAttributes")
    private static DefaultAttributeContainer.Builder createMobAttributes(Operation<DefaultAttributeContainer.Builder> original) {
        return original.call()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, ProgressionModConfig.config.mobChanges.getPolarBearSpeed())
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, ProgressionModConfig.config.mobChanges.getPolarBearRange())
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 50);
    }

    @Definition(id = "shouldAngerAt", method = "Lnet/minecraft/entity/mob/Angerable;shouldAngerAt(Lnet/minecraft/entity/LivingEntity;)Z")
    @Expression("this::shouldAngerAt")
    @ModifyExpressionValue(method = "initGoals", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
    Predicate<LivingEntity> initGoals(Predicate<LivingEntity> original) {
        return livingEntity -> {
            if (ProgressionModConfig.config.mobChanges.getPolarBearAlwaysAggressive()) {
                if (!PolarBearEntityMixin.super.canTarget(livingEntity)) {
                    return false;
                } else {
                    return livingEntity.getType() == EntityType.PLAYER;
                }
            } else {
                return original.test(livingEntity);
            }
        };
    }
}