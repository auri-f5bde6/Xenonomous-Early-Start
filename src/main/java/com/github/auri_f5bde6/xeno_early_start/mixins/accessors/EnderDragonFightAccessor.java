package com.github.auri_f5bde6.xeno_early_start.mixins.accessors;

import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.entity.decoration.EndCrystalEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(EnderDragonFight.class)
public interface EnderDragonFightAccessor {
    @Accessor
    List<EndCrystalEntity> getCrystals();
}
