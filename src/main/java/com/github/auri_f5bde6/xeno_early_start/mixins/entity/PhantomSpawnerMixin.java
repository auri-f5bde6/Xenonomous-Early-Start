package com.github.auri_f5bde6.xeno_early_start.mixins.entity;

import com.github.auri_f5bde6.xeno_early_start.config.XenoEarlyStartConfig;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.spawner.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import static java.lang.Math.min;

@Mixin(PhantomSpawner.class)
public class PhantomSpawnerMixin {
    @Shadow
    private int cooldown;

    // Maybe i should've used the event, but this implementation completely ditch the idea of insomnia, so im not sure how i could've implemented with an event (p.s. im dum)
    @WrapMethod(method = "spawn")
    int customPhantomSpawning(ServerWorld world, boolean spawnMonsters, boolean spawnAnimals, Operation<Integer> original) {
        var count = 0;
        Random random = world.random;
        if (cooldown > 0) {
            cooldown--;
        } else {
            cooldown = cooldown + (60 + random.nextInt(60)) * 20;
            if (world.getAmbientDarkness() < 5 && world.getDimension().hasSkyLight()) {
                return 0;
            } else {
                for (ServerPlayerEntity player : world.getPlayers()) {
                    var playerPos = player.getBlockPos();
                    if (!world.getDimension().hasSkyLight() || (playerPos.getY() >= XenoEarlyStartConfig.config.mobChanges.getPhantomSpawnLevel() || world.getMoonSize() == 1) && playerPos.getY() >= world.getSeaLevel() && world.isSkyVisible(playerPos)) {
                        var difficulty = world.getLocalDifficulty(playerPos);
                        if (difficulty.isHarderThan(random.nextFloat() * 3.0F)) {
                            var spawnPos = playerPos.up(20 + random.nextInt(15)).east(-10 + random.nextInt(21)).south(-10 + random.nextInt(21));
                            if (SpawnHelper.isClearForSpawn(world, spawnPos, world.getBlockState(spawnPos), world.getFluidState(spawnPos), EntityType.PHANTOM)) {
                                EntityData entityData = null;
                                var d = difficulty.getClampedLocalDifficulty();
                                var groupSize = 1 + random.nextInt((int) (d * XenoEarlyStartConfig.config.mobChanges.getOverworldPhantomMaxGroupSize()) + 1);
                                for (int i1 = 0; i1 < groupSize; i1++) {
                                    var phantom = EntityType.PHANTOM.create(world);
                                    if (phantom != null) {
                                        var size = random.nextInt(min((int) (d * XenoEarlyStartConfig.config.mobChanges.getMaxPhantomSize()), 1));
                                        phantom.refreshPositionAndAngles(spawnPos, 0.0F, 0.0F);
                                        entityData = phantom.initialize(world, difficulty, SpawnReason.NATURAL, entityData, null);
                                        phantom.setPhantomSize(size);
                                        world.spawnEntityAndPassengers(phantom);
                                        count++;
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
        return count;
    }
}
