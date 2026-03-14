package com.github.auri_f5bde6.xeno_early_start.mixins.entity;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.spawner.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PhantomSpawner.class)
public class PhantomSpawnerMixin {
    @WrapMethod(method = "spawn")
    int customPhantomSpawning(ServerWorld world, boolean spawnMonsters, boolean spawnAnimals, Operation<Integer> original) {

        return 0;
    }
}
