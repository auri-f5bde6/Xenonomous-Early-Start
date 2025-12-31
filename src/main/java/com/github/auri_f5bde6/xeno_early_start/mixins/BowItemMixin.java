package com.github.auri_f5bde6.xeno_early_start.mixins;

import com.github.auri_f5bde6.xeno_early_start.item.PrimitiveArrowItem;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BowItem.class)
public abstract class BowItemMixin {
    @WrapOperation(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;setVelocity(Lnet/minecraft/entity/Entity;FFFFF)V"))
    public void onStoppedUsing(PersistentProjectileEntity instance, Entity entity, float pitch, float yaw, float roll, float speed, float divergence, Operation<Void> original, @Local(ordinal = 1) ItemStack itemstack) {
        var newSpeed = speed;
        var newDivergence = divergence;
        if (itemstack.getItem() instanceof PrimitiveArrowItem) {
            newSpeed *= 0.75F;
            newDivergence *= 2;
        }
        original.call(instance, entity, pitch, yaw, roll, newSpeed, newDivergence);
    }
}
