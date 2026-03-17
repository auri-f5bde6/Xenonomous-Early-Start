package com.github.auri_f5bde6.xeno_early_start.mixins.entity.brear_or_open_door;

import com.github.auri_f5bde6.xeno_early_start.config.XenoEarlyStartConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.LongDoorInteractGoal;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WitchEntity.class)
public abstract class WitchEntityMixin extends RaiderEntity implements RangedAttackMob {
    protected WitchEntityMixin(EntityType<? extends RaiderEntity> arg, World arg2) {
        super(arg, arg2);
    }

    @Inject(method = "initGoals", at = @At("TAIL"))
    void breakDoor(CallbackInfo ci) {
        if (XenoEarlyStartConfig.config.mobChanges.getWitchCanOpenDoor()) {
            ((MobNavigation) this.getNavigation()).setCanPathThroughDoors(true);
            this.goalSelector.add(1, new LongDoorInteractGoal(this, false));
        }
    }
}
