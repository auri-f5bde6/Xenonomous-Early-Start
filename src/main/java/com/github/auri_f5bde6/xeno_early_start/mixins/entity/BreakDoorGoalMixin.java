package com.github.auri_f5bde6.xeno_early_start.mixins.entity;

import com.github.auri_f5bde6.xeno_early_start.entity.goal.NonZombieBreakDoorGoal;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.entity.ai.goal.BreakDoorGoal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BreakDoorGoal.class)
public class BreakDoorGoalMixin {
    @WrapWithCondition(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;syncWorldEvent(ILnet/minecraft/util/math/BlockPos;I)V", ordinal = 0))
    boolean notZombie(World instance, int i, BlockPos blockPos, int j) {
        return !((BreakDoorGoal) (Object) this instanceof NonZombieBreakDoorGoal);
    }

    @WrapWithCondition(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;syncWorldEvent(ILnet/minecraft/util/math/BlockPos;I)V", ordinal = 1))
    boolean notZombie2(World instance, int i, BlockPos blockPos, int j) {
        return !((BreakDoorGoal) (Object) this instanceof NonZombieBreakDoorGoal);
    }
}
