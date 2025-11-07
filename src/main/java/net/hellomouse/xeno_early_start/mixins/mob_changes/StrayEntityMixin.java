package net.hellomouse.xeno_early_start.mixins.mob_changes;

import com.llamalad7.mixinextras.sugar.Local;
import net.hellomouse.xeno_early_start.registries.XenoEarlyStartStatusEffectRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.StrayEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StrayEntity.class)
public abstract class StrayEntityMixin extends AbstractSkeletonEntity {

    protected StrayEntityMixin(EntityType<? extends AbstractSkeletonEntity> arg, World arg2) {
        super(arg, arg2);
    }

    @Inject(method = "createArrowProjectile", at = @At("TAIL"))
    void createArrowProjectile(ItemStack arrow, float damageModifier, CallbackInfoReturnable<PersistentProjectileEntity> cir, @Local PersistentProjectileEntity persistentProjectileEntity) {
        ((ArrowEntity) persistentProjectileEntity).addEffect(new StatusEffectInstance(XenoEarlyStartStatusEffectRegistry.FREEZING.get(), 100));
    }
}
