package com.github.auri_f5bde6.xeno_early_start.mixins.mob_changes.angerable_changes;

import com.github.auri_f5bde6.xeno_early_start.entity.goal.SquidAttackGoal;
import com.github.auri_f5bde6.xeno_early_start.utils.OtherUtils;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.UUID;

@Mixin(SquidEntity.class)
public abstract class SquidEntityMixin extends WaterCreatureEntity implements Angerable {
    protected SquidEntityMixin(EntityType<? extends WaterCreatureEntity> arg, World arg2, int angerTime, @Nullable UUID angryAt) {
        super(arg, arg2);
    }

    @WrapMethod(method = "createSquidAttributes")
    private static DefaultAttributeContainer.Builder addSquidAttributes(Operation<DefaultAttributeContainer.Builder> original) {
        return original.call()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 5)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 0);
    }

    @Inject(method = "initGoals", at = @At("TAIL"))
    private void initGoals(CallbackInfo ci) {
        this.targetSelector.add(-2, new RevengeGoal(this));
        this.targetSelector.add(-1, new ActiveTargetGoal<>((SquidEntity) (Object) this, PlayerEntity.class, 10, true, false,
                livingEntity -> this.shouldAngerAt(livingEntity) || OtherUtils.isLivingEntityWeak(livingEntity))
        );
        this.targetSelector.add(2, new UniversalAngerGoal<>(this, true));
    }


    @WrapOperation(method = "initGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/goal/GoalSelector;add(ILnet/minecraft/entity/ai/goal/Goal;)V", ordinal = 1))
    private void doNotRun(GoalSelector instance, int priority, Goal goal, Operation<Void> original) {
        this.goalSelector.add(-1, new SquidAttackGoal((SquidEntity) (Object) this));
    }
}
