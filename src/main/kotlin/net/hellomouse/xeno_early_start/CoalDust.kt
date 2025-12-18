package net.hellomouse.xeno_early_start

import net.hellomouse.xeno_early_start.registries.XenoProgressionModParticleRegistry
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
import kotlin.math.min

object CoalDust {
    @JvmStatic
    fun mining(world: ServerWorld, state: BlockState, pos: BlockPos) {
        for (i in 0..<world.random.nextBetween(5, 10)) {
            world.spawnParticles(
                XenoProgressionModParticleRegistry.COAL_DUST.get(),
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
        val destroy = ArrayList<BlockPos>()
        destroy.add(blockPos)
        var index = 0
        val explode = if (alwaysDetonate) {
            true
        } else {
            shouldExplode(world, blockPos)
        }
        while (index < destroy.size) {
            val pos = destroy[index]

            val range = 1
            for (i in -range..range) {
                for (j in -range..range) {
                    for (k in -range..range) {
                        val testPos = pos.add(i, j, k)

                        val b = world.getBlockState(testPos)
                        if (b.isIn(Tags.Blocks.ORES_COAL) && !destroy.contains(testPos)) {
                            destroy.add(testPos)

                        }

                    }
                }
            }
            index++
        }
        if (explode) {
            var i = 0
            while (i < destroy.size) {
                val center = BlockPos.Mutable()
                val clusterSize = min(5, destroy.size - i)
                for (j in 0..<clusterSize) {
                    world.breakBlock(destroy[i], false)
                    center.x += destroy[i].x
                    center.y += destroy[i].y
                    center.z += destroy[i].z
                    i++
                }
                center.x /= clusterSize
                center.y /= clusterSize
                center.z /= clusterSize
                repeat(100) {
                    world.spawnParticles(
                        XenoProgressionModParticleRegistry.COAL_DUST.get(),
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
                val box = Box(center.add(-2, -2, -2).multiply(clusterSize), center.add(2, 2, 2).multiply(clusterSize))
                world.getEntitiesByClass(LivingEntity::class.java, box) { entity ->
                    if (entity.pos.squaredDistanceTo(Vec3d.of(center)) <= 9) {
                        entity.addStatusEffect(StatusEffectInstance(StatusEffects.WITHER, 100))
                        entity.addStatusEffect(StatusEffectInstance(StatusEffects.MINING_FATIGUE, 200))
                        entity.addStatusEffect(StatusEffectInstance(StatusEffects.BLINDNESS, 1000))
                    }
                    return@getEntitiesByClass false
                }
                world.createExplosion(
                    owner,
                    center.x.toDouble(),
                    center.y.toDouble(),
                    center.z.toDouble(),
                    1.5f * log2(destroy.size + 1f),
                    true,
                    World.ExplosionSourceType.NONE
                )
            }
        }
    }

    private fun canTriggerExplosion(state: BlockState): Boolean {
        if (state.isIn(ProgressionModTags.Blocks.ALWAYS_TRIGGER_EXPLOSION)) {
            return true
        }
        if (state.isIn(ProgressionModTags.Blocks.TRIGGER_EXPLOSION_WHEN_LIT)) {
            val lit: Boolean
            try {
                lit = state.get(Properties.LIT)
                if (lit) {
                    return true
                }
            } catch (_: IllegalArgumentException) {
                ProgressionMod.LOGGER.error("${Registries.BLOCK.getId(state.block)} does not have state LIT, desipe being added to xeno_early_start:trigger_explosion_when_lit tag")
            }
        }
        return false
    }
}