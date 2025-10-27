package net.hellomouse.xeno_early_start.mixins.spider;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.hellomouse.xeno_early_start.SpiderPrematureOptimisationInterface;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.mob.SpiderEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(ActiveTargetGoal.class)
public abstract class ActiveTargetGoalMixin {
    @Shadow
    @Nullable
    protected LivingEntity targetEntity;

    @Shadow
    protected abstract void findClosestTarget();

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
