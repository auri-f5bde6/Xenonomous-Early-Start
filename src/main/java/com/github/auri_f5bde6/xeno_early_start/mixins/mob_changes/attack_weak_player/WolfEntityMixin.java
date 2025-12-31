package com.github.auri_f5bde6.xeno_early_start.mixins.mob_changes.attack_weak_player;

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStartConfig;
import com.github.auri_f5bde6.xeno_early_start.utils.OtherUtils;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Predicate;

import static net.minecraft.sound.SoundEvents.ENTITY_WOLF_HOWL;

@Mixin(WolfEntity.class)
public abstract class WolfEntityMixin extends TameableEntity implements Angerable {

    @Unique
    boolean xeno_early_start$nightHowled = false;

    protected WolfEntityMixin(EntityType<? extends TameableEntity> arg, World arg2) {
        super(arg, arg2);
    }

    @Definition(id = "shouldAngerAt", method = "Lnet/minecraft/entity/mob/Angerable;shouldAngerAt(Lnet/minecraft/entity/LivingEntity;)Z")
    @Expression("this::shouldAngerAt")
    @ModifyExpressionValue(method = "initGoals", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
    Predicate<LivingEntity> initGoals(Predicate<LivingEntity> original) {
        return livingEntity -> {
            var world = this.getWorld();
            var isNight = world.isNight();
            if (!isNight) {
                xeno_early_start$nightHowled = false;
            }
            var isWeak = OtherUtils.isLivingEntityWeak(livingEntity);
            var nightHostility = isNight && world.random.nextFloat() <= 0.7;
            if ((isNight && !xeno_early_start$nightHowled) || isWeak) {
                if (isNight) {
                    xeno_early_start$nightHowled = true;
                }
                this.getWorld().playSoundFromEntity(null, this, ENTITY_WOLF_HOWL, SoundCategory.HOSTILE, 1.0F, 1.0F);
            }
            return original.test(livingEntity) || (nightHostility && XenoEarlyStartConfig.config.mobChanges.getWolfAggressiveAtNight()) || livingEntity.isBaby() || isWeak;
        };
    }
}
