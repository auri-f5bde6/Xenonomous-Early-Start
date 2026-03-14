package com.github.auri_f5bde6.xeno_early_start.mixins.entity.neutral_till_fed;

import com.github.auri_f5bde6.xeno_early_start.capabilities.NeutralTilFedData;
import com.github.auri_f5bde6.xeno_early_start.config.XenoEarlyStartConfig;
import com.github.auri_f5bde6.xeno_early_start.entity.goal.DomesticatableActiveTargetGoal;
import com.github.auri_f5bde6.xeno_early_start.entity.goal.DomesticatableAttackGoal;
import com.github.auri_f5bde6.xeno_early_start.entity.goal.DomesticatableRevengeGoal;
import com.github.auri_f5bde6.xeno_early_start.entity.goal.RunawayFromPlayerGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemSteerable;
import net.minecraft.entity.Saddleable;
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
public abstract class NeutralTilFedPigEntityMixin extends TillFedSharedMixin implements ItemSteerable, Saddleable, Angerable {

    protected NeutralTilFedPigEntityMixin(EntityType<? extends PassiveEntity> arg, World arg2) {
        super(arg, arg2);
    }

    @Inject(method = "initGoals", at = @At("TAIL"))
    private void initGoals(CallbackInfo ci) {
        var data = NeutralTilFedData.get(((PigEntity) (Object) this));
        assert data != null;

        if (XenoEarlyStartConfig.config.mobChanges.getAngerablePig()) {
            this.targetSelector.add(1, new DomesticatableRevengeGoal((PigEntity) (Object) this).setGroupRevenge());
            this.targetSelector.add(0, new DomesticatableActiveTargetGoal<>((PigEntity) (Object) this, PlayerEntity.class, 10, true, false,
                    livingEntity -> (this.shouldAngerAt(livingEntity) && !data.getFed()))
            );
            this.goalSelector.add(0, new DomesticatableAttackGoal((PigEntity) (Object) this));
        }
        this.goalSelector.add(1, new RunawayFromPlayerGoal((PigEntity) (Object) this, 1.25, data::getFed));
    }
}
