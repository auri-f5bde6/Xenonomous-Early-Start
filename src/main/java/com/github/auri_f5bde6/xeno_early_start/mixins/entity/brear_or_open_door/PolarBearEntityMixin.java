package com.github.auri_f5bde6.xeno_early_start.mixins.entity.brear_or_open_door;

import com.github.auri_f5bde6.xeno_early_start.config.XenoEarlyStartConfig;
import com.github.auri_f5bde6.xeno_early_start.entity.goal.BreakGlassGoal;
import com.github.auri_f5bde6.xeno_early_start.entity.goal.NonZombieBreakDoorGoal;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.Predicate;

@Mixin(PolarBearEntity.class)
public abstract class PolarBearEntityMixin extends AnimalEntity implements Angerable {

    @Shadow
    @Nullable
    private UUID angryAt;

    protected PolarBearEntityMixin(EntityType<? extends AnimalEntity> arg, World arg2) {
        super(arg, arg2);
    }

    @WrapMethod(method = "createPolarBearAttributes")
    private static DefaultAttributeContainer.Builder createMobAttributes(Operation<DefaultAttributeContainer.Builder> original) {
        return original.call()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, XenoEarlyStartConfig.config.mobChanges.getPolarBearSpeed())
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, XenoEarlyStartConfig.config.mobChanges.getPolarBearRange())
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 50);
    }

    @Definition(id = "shouldAngerAt", method = "Lnet/minecraft/entity/mob/Angerable;shouldAngerAt(Lnet/minecraft/entity/LivingEntity;)Z")
    @Expression("this::shouldAngerAt")
    @ModifyExpressionValue(method = "initGoals", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
    Predicate<LivingEntity> initGoals(Predicate<LivingEntity> original) {
        return livingEntity -> {
            if (XenoEarlyStartConfig.config.mobChanges.getPolarBearAlwaysAggressive()) {
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

    @Inject(method = "initGoals", at = @At("TAIL"))
    void breakDoorGoal(CallbackInfo ci) {
        ((MobNavigation) this.getNavigation()).setCanPathThroughDoors(true);
        this.goalSelector.add(1, new NonZombieBreakDoorGoal(this, (difficulty) -> this.angryAt != null, 20));
        this.goalSelector.add(1, new BreakGlassGoal(this, this::hasAngerTime));
    }
}