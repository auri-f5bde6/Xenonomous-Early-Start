package com.github.auri_f5bde6.xeno_early_start.mixins.entity.neutral_till_fed;

import com.github.auri_f5bde6.xeno_early_start.capabilities.NeutralTilFedData;
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
        var cap = NeutralTilFedData.get(mob);
        if (cap != null && !cap.getFed()) {
            return false;
        }
        return original.call();
    }
}
