package com.github.auri_f5bde6.xeno_early_start

import com.github.auri_f5bde6.xeno_early_start.entity.CoalDustExplosion
import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartParticleRegistry
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.registry.Registries
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraftforge.common.Tags
import kotlin.math.log2
import kotlin.math.pow

object CoalDust {
    @JvmStatic
    fun mining(world: ServerWorld, state: BlockState, pos: BlockPos) {
        applyStatusEffect(world, pos, 2, listOf(pos))
        for (i in 0..<world.random.nextBetween(5, 10)) {
            world.spawnParticles(
                XenoEarlyStartParticleRegistry.COAL_DUST.get(),
                pos.x - 0.1 + world.random.nextFloat() * 1.1,
                pos.y - 0.1 + world.random.nextFloat() * 1.1,
                pos.z - 0.1 + world.random.nextFloat() * 1.1,
                1,
                0.0,
                -0.01,
                0.0,
                0.005
            )
        }
    }

    fun shouldExplode(world: World, centerPos: BlockPos): Boolean {
        val range = 4
        for (i in -range..range) {
            for (j in -range..range) {
                for (k in -range..range) {
                    val testPos = centerPos.add(i, j, k)
                    val dist = testPos.getSquaredDistance(centerPos)
                    if (dist <= 25) {
                        val b = world.getBlockState(testPos)
                        if (canTriggerExplosion(b)
                        ) {
                            return true
                        }
                    }

                }
            }
        }
        return false
    }

    fun tryDetonate(world: ServerWorld, blockPos: BlockPos, alwaysDetonate: Boolean, owner: Entity?) {
        if (!world.getBlockState(blockPos).isIn(Tags.Blocks.ORES_COAL)) {
            return
        }
        val explode = if (alwaysDetonate) {
            true
        } else {
            shouldExplode(world, blockPos)
        }
        if (!explode) {
            return
        }
        applyStatusEffect(world, blockPos, 3, listOf(blockPos))
        var count = 1
        var index = 0
        val collection =
            MutableList<BlockPos?>(XenoEarlyStartConfig.config.oreChanges.coalDustExplosionClusterSize) { null }
        val destroy = ArrayList<BlockPos>()
        destroy.add(blockPos)
        collection[0] = blockPos
        while (destroy.size in (index + 1)..XenoEarlyStartConfig.config.oreChanges.coalDustExplosionBlockLimit) {
            val pos = destroy[index]
            for (i in -1..1) {
                for (j in -1..1) {
                    for (k in -1..1) {
                        val testPos = pos.add(i, j, k)
                        val b = world.getBlockState(testPos)
                        if (b.isIn(Tags.Blocks.ORES_COAL) && !destroy.contains(testPos)) {
                            val currentIndex =
                                count % XenoEarlyStartConfig.config.oreChanges.coalDustExplosionClusterSize
                            destroy.add(testPos)
                            collection[currentIndex] = testPos
                            if (currentIndex == XenoEarlyStartConfig.config.oreChanges.coalDustExplosionClusterSize - 1) {
                                createExplosionAtCluster(owner, world, collection)
                            }
                            count++
                        }

                    }
                }
            }
            index++
        }
        val currentIndex = count % XenoEarlyStartConfig.config.oreChanges.coalDustExplosionClusterSize
        if (currentIndex > 0) {
            createExplosionAtCluster(owner, world, collection.subList(0, currentIndex))
        }
    }

    private fun createExplosionAtCluster(owner: Entity?, world: ServerWorld, blockPoss: List<BlockPos?>) {
        val center = BlockPos.Mutable()
        var size = 0
        for (pos in blockPoss) {
            if (pos != null) {
                size++
                world.breakBlock(pos, false)
                center.x += pos.x
                center.y += pos.y
                center.z += pos.z
            }
        }
        center.x /= size
        center.y /= size
        center.z /= size
        repeat(100) {
            world.spawnParticles(
                XenoEarlyStartParticleRegistry.COAL_DUST.get(),
                center.x - 0.1 + world.random.nextFloat() * 1.06,
                center.y - 0.1 + world.random.nextFloat() * 1.5,
                center.z - 0.1 + world.random.nextFloat() * 1.06,
                1,
                world.random.nextFloat() * 1.5,
                -0.01,
                world.random.nextFloat() * 1.5,
                0.005
            )
        }
        CoalDustExplosion.createExplosion(
            world,
            owner,
            null,
            null,
            center.x.toDouble(),
            center.y.toDouble(),
            center.z.toDouble(),
            1.5f * log2(size + 1f),
            createFire = true,
            particles = true
        )
    }

    private fun canTriggerExplosion(state: BlockState): Boolean {
        if (state.isIn(XenoEarlyStartTags.Blocks.ALWAYS_TRIGGER_EXPLOSION)) {
            return true
        }
        if (state.isIn(XenoEarlyStartTags.Blocks.TRIGGER_EXPLOSION_WHEN_LIT)) {
            val lit: Boolean
            try {
                lit = state.get(Properties.LIT)
                if (lit) {
                    return true
                }
            } catch (_: IllegalArgumentException) {
                XenoEarlyStart.LOGGER.error("${Registries.BLOCK.getId(state.block)} does not have state LIT, desipe being added to xeno_early_start:trigger_explosion_when_lit tag")
            }
        }
        return false
    }

    fun applyStatusEffect(world: World, center: BlockPos, size: Int, blockPoss: List<BlockPos?>) {
        val box = Box(center.add(-(size + 1), -(size + 1), -(size + 1)), center.add((size + 1), (size + 1), (size + 1)))
        world.getEntitiesByClass(LivingEntity::class.java, box) { entity ->
            for (pos in blockPoss) {
                if (pos != null && entity.pos.squaredDistanceTo(Vec3d.of(pos)) <= size.toDouble().pow(2.0).toInt()) {
                    entity.addStatusEffect(StatusEffectInstance(StatusEffects.WITHER, 100))
                    entity.addStatusEffect(StatusEffectInstance(StatusEffects.MINING_FATIGUE, 200))
                    entity.addStatusEffect(StatusEffectInstance(StatusEffects.BLINDNESS, 1000))
                }
            }
            return@getEntitiesByClass false
        }
    }
}