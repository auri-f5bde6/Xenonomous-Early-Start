package com.github.auri_f5bde6.xeno_early_start.mixins.entity.brear_or_open_door;

import com.github.auri_f5bde6.xeno_early_start.config.XenoEarlyStartConfig;
import com.github.auri_f5bde6.xeno_early_start.entity.goal.NonZombieBreakDoorGoal;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WolfEntity.class)
public abstract class WolfEntityMixin extends TameableEntity implements Angerable {
    protected WolfEntityMixin(EntityType<? extends TameableEntity> arg, World arg2) {
        super(arg, arg2);
    }

    @Inject(method = "initGoals", at = @At("TAIL"))
    void breakDoor(CallbackInfo ci) {
        if (XenoEarlyStartConfig.config.mobChanges.getAngryWolfCanDestroyDoor()) {
            ((MobNavigation) this.getNavigation()).setCanPathThroughDoors(true);
            this.goalSelector.add(1, new NonZombieBreakDoorGoal(this, (difficulty) -> hasAngerTime() && difficulty == Difficulty.HARD, 240));
        }
    }

    @WrapMethod(method = "setTamed")
    void noLongerPathfindThroughDoor(boolean tamed, Operation<Void> original) {
        ((MobNavigation) this.getNavigation()).setCanPathThroughDoors(!tamed);
        original.call(tamed);
    }
}
