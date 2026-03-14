package com.github.auri_f5bde6.xeno_early_start.mixins.entity.neutral_till_fed;

import com.github.auri_f5bde6.xeno_early_start.config.XenoEarlyStartConfig;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemSteerable;
import net.minecraft.entity.Saddleable;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PigEntity.class)
public abstract class AngerablePigEntityMixin extends TillFedSharedMixin implements ItemSteerable, Saddleable, Angerable {

    protected AngerablePigEntityMixin(EntityType<? extends PassiveEntity> arg, World arg2) {
        super(arg, arg2);
    }

    @WrapMethod(method = "createPigAttributes")
    private static DefaultAttributeContainer.Builder addSPigAttributes(Operation<DefaultAttributeContainer.Builder> original) {
        return original.call()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 0.5);
    }

    @Inject(method = "initGoals", at = @At("TAIL"))
    private void initGoals(CallbackInfo ci) {
        if (!XenoEarlyStartConfig.config.mobChanges.getPigRunAwayFromPlayerUntilFed()) {
            this.targetSelector.add(1, new RevengeGoal((PigEntity) (Object) this).setGroupRevenge());
            this.targetSelector.add(0, new ActiveTargetGoal<>((PigEntity) (Object) this, PlayerEntity.class, 10, true, false,
                    livingEntity -> true)
            );
        }

    }
}
