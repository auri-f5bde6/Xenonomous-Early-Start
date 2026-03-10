package com.github.auri_f5bde6.xeno_early_start.mixins.mob_changes.neutral_till_fed;

import com.github.auri_f5bde6.xeno_early_start.capabilities.NeutralTilFedData;
import com.github.auri_f5bde6.xeno_early_start.entity.goal.DomesticatableEscapeDangerGoal;
import com.github.auri_f5bde6.xeno_early_start.entity.goal.RunawayFromPlayerGoal;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

@Mixin(SheepEntity.class)
public abstract class SheepEntityMixin extends TillFedSharedMixin {
    protected SheepEntityMixin(EntityType<? extends LivingEntity> arg, World arg2) {
        super(arg, arg2);
    }

    @WrapMethod(method = "initGoals")
    protected void initGoals(Operation<Void> original) {
        var sheep = (SheepEntity) (Object) this;
        this.goalSelector.add(1, new RunawayFromPlayerGoal(sheep, 1.25,
                () -> Objects.requireNonNull(NeutralTilFedData.get(sheep)).getHasBeenFed()
        ));
        this.goalSelector.add(1, new DomesticatableEscapeDangerGoal(sheep));
        original.call();
    }

    @WrapOperation(method = "initGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/goal/GoalSelector;add(ILnet/minecraft/entity/ai/goal/Goal;)V", ordinal = 1))
    private void doNotRun(GoalSelector instance, int priority, Goal goal, Operation<Void> original) {

    }
}
