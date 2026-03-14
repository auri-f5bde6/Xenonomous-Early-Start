package com.github.auri_f5bde6.xeno_early_start.mixins.entity.neutral_till_fed;

import com.github.auri_f5bde6.xeno_early_start.capabilities.NeutralTilFedData;
import com.github.auri_f5bde6.xeno_early_start.entity.goal.RunawayFromPlayerGoal;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Objects;

@Mixin(SheepEntity.class)
public abstract class SheepEntityMixin extends TillFedSharedMixin {
    protected SheepEntityMixin(EntityType<? extends PassiveEntity> arg, World arg2) {
        super(arg, arg2);
    }

    @WrapMethod(method = "initGoals")
    protected void initGoals(Operation<Void> original) {
        var sheep = (SheepEntity) (Object) this;
        this.goalSelector.add(1, new RunawayFromPlayerGoal(sheep, 1.25,
                () -> Objects.requireNonNull(NeutralTilFedData.get(sheep)).getFed()
        ));
        original.call();
    }
}
