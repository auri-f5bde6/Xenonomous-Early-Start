package net.hellomouse.xeno_early_start.mixins.mob_changes.neutral_till_fed;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.hellomouse.xeno_early_start.entity.goal.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemSteerable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Saddleable;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PigEntity.class)
public abstract class PigEntityMixin extends TillFedSharedMixin implements ItemSteerable, Saddleable, Angerable {

    @Unique
    private static final TrackedData<Boolean> xeno_early_start$FEED = DataTracker.registerData(PigEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    protected PigEntityMixin(EntityType<? extends LivingEntity> arg, World arg2) {
        super(arg, arg2);
    }

    @WrapMethod(method = "createPigAttributes")
    private static DefaultAttributeContainer.Builder addSquidAttributes(Operation<DefaultAttributeContainer.Builder> original) {
        return original.call()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 5)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 0.5);
    }

    @Override
    public TrackedData<Boolean> xeno_early_start$getFeedTrackedData() {
        return xeno_early_start$FEED;
    }

    @Inject(method = "initGoals", at = @At("TAIL"))
    private void initGoals(CallbackInfo ci) {
        this.targetSelector.add(1, new DomesticatableRevengeGoal((PigEntity) (Object) this));
        this.targetSelector.add(0, new DomesticatableActiveTargetGoal<>((PigEntity) (Object) this, PlayerEntity.class, 10, true, false,
                livingEntity -> (this.shouldAngerAt(livingEntity) && !xeno_early_start$haveBeenFed()))
        );
        this.goalSelector.add(1, new RunawayFromPlayerGoal((PigEntity) (Object) this, 1.25, xeno_early_start$FEED));
        this.goalSelector.add(0, new DomesticatableAttackGoal((PigEntity) (Object) this));
        this.goalSelector.add(1, new DomesticatableEscapeDangerGoal((PigEntity) (Object) this));

    }


    @WrapOperation(method = "initGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/goal/GoalSelector;add(ILnet/minecraft/entity/ai/goal/Goal;)V", ordinal = 1))
    private void doNotRun(GoalSelector instance, int priority, Goal goal, Operation<Void> original) {

    }
}
