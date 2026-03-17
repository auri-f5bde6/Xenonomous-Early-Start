package com.github.auri_f5bde6.xeno_early_start.mixins.entity.brear_or_open_door;

import com.github.auri_f5bde6.xeno_early_start.config.XenoEarlyStartConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.mob.IllusionerEntity;
import net.minecraft.entity.mob.SpellcastingIllagerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IllusionerEntity.class)
public abstract class IllusionerEntityMixin extends SpellcastingIllagerEntity implements RangedAttackMob {
    protected IllusionerEntityMixin(EntityType<? extends SpellcastingIllagerEntity> arg, World arg2) {
        super(arg, arg2);
    }

    @Inject(method = "initGoals", at = @At("TAIL"))
    void breakDoor(CallbackInfo ci) {
        if (XenoEarlyStartConfig.config.mobChanges.getIllusionerCanOpenDoor()) {
            ((MobNavigation) this.getNavigation()).setCanPathThroughDoors(true);
            this.goalSelector.add(1, new LongDoorInteractGoal(this));
        }
    }
}
