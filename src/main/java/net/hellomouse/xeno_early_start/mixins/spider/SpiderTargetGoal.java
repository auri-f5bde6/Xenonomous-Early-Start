package net.hellomouse.xeno_early_start.mixins.spider;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.hellomouse.xeno_early_start.SpiderPrematureOptimisationInterface;
import net.hellomouse.xeno_early_start.utils.OtherUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SpiderEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SpiderEntity.TargetGoal.class)
public abstract class SpiderTargetGoal<T extends LivingEntity> extends ActiveTargetGoal<T> implements SpiderPrematureOptimisationInterface {
    @Unique
    boolean xeno_early_start$findClosestTargetCalled = false;

    public SpiderTargetGoal(MobEntity mob, Class<T> targetClass, boolean checkVisibility) {
        super(mob, targetClass, checkVisibility);

    }

    @WrapMethod(method = "canStart")
    boolean canStart(Operation<Boolean> original) {
        this.setXeno_early_start$findClosestTargetCalled(false);
        this.findClosestTarget();
        this.setXeno_early_start$findClosestTargetCalled(true);
        return original.call() || OtherUtils.isLivingEntityWeak(this.targetEntity);
    }

    @Override
    public boolean getXeno_early_start$findClosestTargetCalled() {
        return xeno_early_start$findClosestTargetCalled;
    }

    @Override
    public void setXeno_early_start$findClosestTargetCalled(boolean value) {
        xeno_early_start$findClosestTargetCalled = value;
    }
}
