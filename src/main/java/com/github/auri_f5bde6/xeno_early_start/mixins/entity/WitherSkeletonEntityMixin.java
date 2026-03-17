package com.github.auri_f5bde6.xeno_early_start.mixins.entity;

import com.github.auri_f5bde6.xeno_early_start.entity.goal.NonZombieBreakDoorGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WitherSkeletonEntity.class)
public abstract class WitherSkeletonEntityMixin extends AbstractSkeletonEntity {
    protected WitherSkeletonEntityMixin(EntityType<? extends AbstractSkeletonEntity> arg, World arg2) {
        super(arg, arg2);
    }

    @Inject(method = "initGoals", at = @At("TAIL"))
    void breakDoor(CallbackInfo ci) {
        ((MobNavigation) this.getNavigation()).setCanPathThroughDoors(true);
        this.goalSelector.add(1, new NonZombieBreakDoorGoal(this, (difficulty) -> difficulty == Difficulty.HARD, 100));
    }
}
