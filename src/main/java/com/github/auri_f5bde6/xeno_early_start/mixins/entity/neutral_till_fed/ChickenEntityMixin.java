package com.github.auri_f5bde6.xeno_early_start.mixins.entity.neutral_till_fed;

import com.github.auri_f5bde6.xeno_early_start.capabilities.NeutralTilFedData;
import com.github.auri_f5bde6.xeno_early_start.entity.goal.RunawayFromPlayerGoal;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ChickenEntity.class)
public abstract class ChickenEntityMixin extends TillFedSharedMixin {

    protected ChickenEntityMixin(EntityType<? extends PassiveEntity> arg, World arg2) {
        super(arg, arg2);
    }

    @WrapMethod(method = "initGoals")
    protected void initGoals(Operation<Void> original) {
        var data = NeutralTilFedData.get(((AnimalEntity) (Object) this));
        assert data != null;
        this.goalSelector.add(1, new RunawayFromPlayerGoal((ChickenEntity) (Object) this, 1.4, data::getFed));
        original.call();
    }
}
