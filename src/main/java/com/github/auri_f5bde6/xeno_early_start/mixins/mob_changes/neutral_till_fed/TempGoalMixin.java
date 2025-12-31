package com.github.auri_f5bde6.xeno_early_start.mixins.mob_changes.neutral_till_fed;

import com.github.auri_f5bde6.xeno_early_start.TillFedInterface;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TemptGoal.class)
public class TempGoalMixin {
    @Shadow
    @Final
    protected PathAwareEntity mob;

    @WrapMethod(method = "canStart")
    public boolean canStart(Operation<Boolean> original) {
        var tilFedMob = (TillFedInterface) this.mob;
        if (tilFedMob.xeno_early_start$hasFeedTrackedData() && !tilFedMob.xeno_early_start$haveBeenFed()) {
            return false;
        }
        return original.call();
    }
}
