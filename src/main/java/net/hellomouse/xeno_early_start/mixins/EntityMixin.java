package net.hellomouse.xeno_early_start.mixins;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    protected final Random random = Random.create();
}
