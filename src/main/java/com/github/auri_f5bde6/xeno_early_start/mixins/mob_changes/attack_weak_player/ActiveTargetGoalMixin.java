package com.github.auri_f5bde6.xeno_early_start.mixins.mob_changes.attack_weak_player;

import com.github.auri_f5bde6.xeno_early_start.SpiderPrematureOptimisationInterface;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.mob.SpiderEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ActiveTargetGoal.class)
public abstract class ActiveTargetGoalMixin {

    @WrapMethod(method = "findClosestTarget")
    void dontFindClosestTargetMoreThanOnce(Operation<Void> original) {
        if ((ActiveTargetGoal) (Object) this instanceof SpiderEntity.TargetGoal) {
            if (((SpiderPrematureOptimisationInterface) this).getXeno_early_start$findClosestTargetCalled()) {
                ((SpiderPrematureOptimisationInterface) this).setXeno_early_start$findClosestTargetCalled(false);
            } else {
                original.call();
            }
        } else {
            original.call();
        }

    }

}
