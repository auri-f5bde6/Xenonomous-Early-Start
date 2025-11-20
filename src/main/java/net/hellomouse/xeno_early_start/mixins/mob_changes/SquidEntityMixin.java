package net.hellomouse.xeno_early_start.mixins.mob_changes;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.hellomouse.xeno_early_start.entity.goal.SquidAttackGoal;
import net.hellomouse.xeno_early_start.utils.OtherUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.UUID;

@Mixin(SquidEntity.class)
public abstract class SquidEntityMixin extends WaterCreatureEntity implements Angerable {
    @Unique
    private static final UniformIntProvider ANGER_TIME_RANGE = TimeHelper.betweenSeconds(20, 39);
    @Unique
    private int xeno_early_start$angerTime;
    @Unique
    @Nullable
    private UUID xeno_early_start$angryAt;

    protected SquidEntityMixin(EntityType<? extends WaterCreatureEntity> arg, World arg2, int angerTime, @Nullable UUID angryAt) {
        super(arg, arg2);
        this.xeno_early_start$angerTime = angerTime;
        this.xeno_early_start$angryAt = angryAt;
    }

    @WrapMethod(method = "createSquidAttributes")
    private static DefaultAttributeContainer.Builder addSquidAttributes(Operation<DefaultAttributeContainer.Builder> original) {

        return original.call()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 5)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 0);
    }

    @Inject(method = "initGoals", at = @At("TAIL"))
    private void initGoals(CallbackInfo ci) {
        this.targetSelector.add(-2, new RevengeGoal(this));
        this.targetSelector.add(-1, new ActiveTargetGoal<>((SquidEntity) (Object) this, PlayerEntity.class, 10, true, false,
                livingEntity -> this.shouldAngerAt(livingEntity) || OtherUtils.isLivingEntityWeak(livingEntity))
        );
        this.targetSelector.add(2, new UniversalAngerGoal<>(this, true));
    }

    @Override
    public void chooseRandomAngerTime() {
        this.setAngerTime(ANGER_TIME_RANGE.get(this.random));
    }

    @Override
    public int getAngerTime() {
        return this.xeno_early_start$angerTime;
    }

    @Override
    public void setAngerTime(int angerTime) {
        this.xeno_early_start$angerTime = angerTime;
    }

    @Nullable
    @Override
    public UUID getAngryAt() {
        return this.xeno_early_start$angryAt;
    }

    @Override
    public void setAngryAt(@Nullable UUID angryAt) {
        this.xeno_early_start$angryAt = angryAt;
    }

    @WrapOperation(method = "initGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/goal/GoalSelector;add(ILnet/minecraft/entity/ai/goal/Goal;)V", ordinal = 1))
    private void doNotRun(GoalSelector instance, int priority, Goal goal, Operation<Void> original) {
        this.goalSelector.add(-1, new SquidAttackGoal((SquidEntity) (Object) this));
    }

    @Inject(method = "tickMovement", at = @At("TAIL"))
    void tickMovement(CallbackInfo ci) {
        if (!this.getWorld().isClient) {
            this.tickAngerLogic((ServerWorld) this.getWorld(), true);
        }
    }

    @Override
    public boolean tryAttack(Entity target) {
        /*boolean bl = target.damage(this.getDamageSources().mobAttack(this), 10);
        if (bl) {
            this.applyDamageEffects(this, target);
        }

        return bl;*/
        return super.tryAttack(target);
    }

}
