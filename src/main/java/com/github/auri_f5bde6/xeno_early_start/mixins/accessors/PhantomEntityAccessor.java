package com.github.auri_f5bde6.xeno_early_start.mixins.accessors;

import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PhantomEntity.class)
public interface PhantomEntityAccessor {
    @Accessor
    BlockPos getCirclingCenter();

    @Accessor
    void setCirclingCenter(BlockPos circlingCenter);
}
