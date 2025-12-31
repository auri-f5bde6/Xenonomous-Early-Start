package com.github.auri_f5bde6.xeno_early_start.mixins.mob_changes.neutral_till_fed;

import com.github.auri_f5bde6.xeno_early_start.entity.goal.DomesticatableEscapeDangerGoal;
import com.github.auri_f5bde6.xeno_early_start.entity.goal.RunawayFromPlayerGoal;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SheepEntity.class)
public abstract class SheepEntityMixin extends TillFedSharedMixin {
    @Unique
    private static final TrackedData<Boolean> xeno_early_start$FEED = DataTracker.registerData(SheepEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    protected SheepEntityMixin(EntityType<? extends LivingEntity> arg, World arg2) {
        super(arg, arg2);
    }

    @Override
    public TrackedData<Boolean> xeno_early_start$getFeedTrackedData() {
        return xeno_early_start$FEED;
    }

    @WrapMethod(method = "initGoals")
    protected void initGoals(Operation<Void> original) {
        this.goalSelector.add(1, new RunawayFromPlayerGoal((SheepEntity) (Object) this, 1.25, xeno_early_start$FEED));
        this.goalSelector.add(1, new DomesticatableEscapeDangerGoal((SheepEntity) (Object) this));
        original.call();
    }

    @WrapOperation(method = "initGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/goal/GoalSelector;add(ILnet/minecraft/entity/ai/goal/Goal;)V", ordinal = 1))
    private void doNotRun(GoalSelector instance, int priority, Goal goal, Operation<Void> original) {

    }
}
