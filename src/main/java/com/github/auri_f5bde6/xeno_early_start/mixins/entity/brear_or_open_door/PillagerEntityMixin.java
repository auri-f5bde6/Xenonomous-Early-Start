package com.github.auri_f5bde6.xeno_early_start.mixins.entity.brear_or_open_door;

import com.github.auri_f5bde6.xeno_early_start.entity.goal.NonZombieBreakDoorGoal;
import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.InventoryOwner;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PillagerEntity.class)
public abstract class PillagerEntityMixin extends IllagerEntity implements CrossbowUser, InventoryOwner {
    protected PillagerEntityMixin(EntityType<? extends IllagerEntity> arg, World arg2) {
        super(arg, arg2);
    }

    @Inject(method = "initGoals", at = @At("TAIL"))
    void breakDoorGoal(CallbackInfo ci) {
        ((MobNavigation) this.getNavigation()).setCanPathThroughDoors(true);
        this.goalSelector.add(1, new NonZombieBreakDoorGoal(this, (difficulty) -> difficulty == Difficulty.HARD, 200));
    }
}
