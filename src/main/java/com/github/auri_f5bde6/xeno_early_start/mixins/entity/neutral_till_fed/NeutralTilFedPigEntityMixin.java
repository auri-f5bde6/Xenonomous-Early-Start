package com.github.auri_f5bde6.xeno_early_start.mixins.entity.neutral_till_fed;

import com.github.auri_f5bde6.xeno_early_start.capabilities.NeutralTilFedData;
import com.github.auri_f5bde6.xeno_early_start.entity.goal.RunawayFromPlayerGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PigEntity.class)
public abstract class NeutralTilFedPigEntityMixin extends PassiveEntity {

    protected NeutralTilFedPigEntityMixin(EntityType<? extends PassiveEntity> arg, World arg2) {
        super(arg, arg2);
    }

    @Inject(method = "initGoals", at = @At("TAIL"))
    private void initCustomGoals(CallbackInfo ci) {
        var data = NeutralTilFedData.get(((PigEntity) (Object) this));
        assert data != null;
        this.goalSelector.add(1, new RunawayFromPlayerGoal((PigEntity) (Object) this, 1.25, data::getFed));
    }
}
