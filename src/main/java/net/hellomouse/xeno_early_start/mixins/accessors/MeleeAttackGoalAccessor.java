package net.hellomouse.xeno_early_start.mixins.accessors;

import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MeleeAttackGoal.class)
public interface MeleeAttackGoalAccessor {
    @Accessor("cooldown")
    void xeno_early_start$setCooldown(int cooldown);
}