package com.github.auri_f5bde6.xeno_early_start.mixins.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.PhantomEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PhantomEntity.class)
public abstract class PhantomEntityMixin extends MobEntityMixin {
    protected PhantomEntityMixin(boolean onGround) {
        super(onGround);
    }

    @Override
    protected boolean customInvulnerability(DamageSource damageSource, Operation<Boolean> original) {
        if (damageSource.getAttacker() instanceof EnderDragonEntity || damageSource.getAttacker() instanceof EnderDragonPart) {
            return true;
        }
        return super.customInvulnerability(damageSource, original);
    }

}
