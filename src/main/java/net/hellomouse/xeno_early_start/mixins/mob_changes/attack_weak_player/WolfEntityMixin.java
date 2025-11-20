package net.hellomouse.xeno_early_start.mixins.mob_changes.attack_weak_player;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.hellomouse.xeno_early_start.utils.OtherUtils;
import net.minecraft.entity.Entity;
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
    boolean nightHowled = false;

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
                nightHowled = false;
            }
            var isWeak = OtherUtils.isLivingEntityWeak(livingEntity);
            var nightHostility = isNight && world.random.nextFloat() <= 0.7;
            if ((isNight && !nightHowled) || isWeak) {
                if (isNight) {
                    nightHowled = true;
                }
                this.getWorld().playSoundFromEntity(null, (Entity) this, ENTITY_WOLF_HOWL, SoundCategory.HOSTILE, 1.0F, 1.0F);
            }
            return original.test(livingEntity) || isNight || livingEntity.isBaby() || isWeak;
        };
    }
}
