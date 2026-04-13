package com.github.auri_f5bde6.xeno_early_start.mixins;

import com.github.auri_f5bde6.xeno_early_start.config.XenoEarlyStartConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static net.minecraft.world.SpawnHelper.shouldUseNetherFortressSpawns;

@Mixin(SpawnHelper.class)
public class SpawnHelperMixin {
    @WrapOperation(method = "spawnEntitiesInChunk(Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/SpawnHelper$Checker;Lnet/minecraft/world/SpawnHelper$Runner;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/SpawnHelper;createMob(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/EntityType;)Lnet/minecraft/entity/mob/MobEntity;"))
    private static MobEntity replaceSpawn(ServerWorld world, EntityType<?> type, Operation<MobEntity> original, SpawnGroup group, ServerWorld _world, Chunk chunk, BlockPos pos, SpawnHelper.Checker checker, SpawnHelper.Runner runner) {
        if (shouldUseNetherFortressSpawns(
                pos,
                world,
                group,
                world.getStructureAccessor()
        ) && type == EntityType.SKELETON) {
            return original.call(world, EntityType.WITHER_SKELETON);
        } else if (type == EntityType.SPIDER && pos.getY() <= XenoEarlyStartConfig.config.mobChanges.getReplaceSpiderWithCaveSpiderY()) {
            return original.call(world, EntityType.CAVE_SPIDER);
        } else if (type == EntityType.RABBIT && world.random.nextFloat() < 0.001) {
            var rabbitEntity = (RabbitEntity) original.call(world, EntityType.RABBIT);
            if (rabbitEntity == null) {
                return null;
            }
            rabbitEntity.setVariant(RabbitEntity.RabbitType.EVIL);
        }
        return original.call(world, type);
    }
}
