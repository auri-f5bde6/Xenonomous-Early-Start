package net.hellomouse.xeno_early_start

import net.hellomouse.xeno_early_start.registries.XenoProgressionModParticleRegistry
import net.minecraft.block.BlockState
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

    @JvmStatic
    fun mineFinish(world: ServerWorld, state: BlockState, blockPos: BlockPos) {
        val destroy = ArrayList<BlockPos>()
        destroy.add(blockPos)
        var index = 0
        var first = true
        var explode = false
        while (index < destroy.size && (explode || first)) {
            val pos = destroy[index]
            if (first) {
                first = false
            } else {
                world.breakBlock(pos, false)
            }
            index++
            repeat(100) {
                world.spawnParticles(
                    XenoProgressionModParticleRegistry.COAL_DUST.get(),
                    pos.x - 0.1 + world.random.nextFloat() * 1.06,
                    pos.y - 0.1 + world.random.nextFloat() * 1.5,
                    pos.z - 0.1 + world.random.nextFloat() * 1.06,
                    1,
                    world.random.nextFloat() * 1.5,
                    -0.01,
                    world.random.nextFloat() * 1.5,
                    0.005
                )
            }
            val box = Box(pos.add(-2, -2, -2), pos.add(2, 2, 2))
            world.getEntitiesByClass(LivingEntity::class.java, box) { entity ->
                if (entity.pos.squaredDistanceTo(Vec3d.of(pos)) <= 9) {
                    entity.addStatusEffect(StatusEffectInstance(StatusEffects.WITHER, 100))
                    entity.addStatusEffect(StatusEffectInstance(StatusEffects.MINING_FATIGUE, 200))
                }
                return@getEntitiesByClass false
            }
            val range = 4
            for (i in -range..range) {
                for (j in -range..range) {
                    for (k in -range..range) {
                        val testPos = pos.add(i, j, k)
                        val dist = testPos.getSquaredDistance(pos)
                        if (dist <= 25) {
                            val b = world.getBlockState(testPos)
                            if (b.isIn(Tags.Blocks.ORES_COAL) && !destroy.contains(testPos)) {
                                destroy.add(testPos)
                            }
                            if (canTriggerExplosion(b)
                            ) {
                                explode = true
                            }
                        }

                    }
                }
            }
        }
        if (explode) {
            val center = BlockPos.Mutable()
            for (pos in destroy) {
                center.x += pos.x
                center.y += pos.y
                center.z += pos.z
            }
            center.x /= destroy.size
            center.y /= destroy.size
            center.z /= destroy.size
            world.createExplosion(
                null,
                center.x.toDouble(),
                center.y.toDouble(),
                center.z.toDouble(),
                1.5f * log2(destroy.size + 1f),
                true,
                World.ExplosionSourceType.NONE
            )
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