package com.github.auri_f5bde6.xeno_early_start.mixins.entity;

import net.minecraft.entity.mob.PhantomEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PhantomEntity.class)
public abstract class PhantomEntityMixin extends MobEntityMixin {

    protected PhantomEntityMixin(boolean onGround) {
        super(onGround);
    }

}
