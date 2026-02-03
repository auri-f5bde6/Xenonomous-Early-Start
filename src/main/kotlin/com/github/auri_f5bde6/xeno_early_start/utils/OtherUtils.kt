package com.github.auri_f5bde6.xeno_early_start.utils

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStartConfig
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3i
import net.minecraft.world.BlockView
import net.minecraft.world.Heightmap
import net.minecraft.world.RaycastContext
import net.minecraft.world.World

object OtherUtils {
    /**
     *Return (wasBlockHitAccordingToPredicate, wasAnyBlockHit)
     */
    @JvmStatic
    fun raycast(
        world: World,
        start: Vec3d,
        end: Vec3d,
        shouldIgnore: (blockState: BlockState) -> Boolean
    ): Pair<Boolean, Boolean> {
        if (start == end) {
            return Pair(false, false)
        }
        var wasAnyBlockHit = false
        val r = BlockView.raycast(
            start,
            end,
            null,
            { _, hitPos ->
                val blockState = world.getBlockState(hitPos)
                if (!blockState.isAir) {
                    wasAnyBlockHit = true
                }
                // TODO: I have 0 clues why raycasting from -60 to -58 can product a hit at -61 ...
                if (blockState.isAir || hitPos == start || shouldIgnore(blockState)) {
                    null
                } else {
                    false
                }
            },
            { _ -> true })
        return Pair(r!!, wasAnyBlockHit)
    }

    @JvmStatic
    fun isCovered(world: World, pos: BlockPos): Boolean {
        return world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, pos).y > pos.y
    }

    @JvmStatic
    fun raycastSteppingOn(world: World, entityPos: Vec3d, entity: Entity): Pair<BlockPos, BlockState> {
        val divisor = 4
        val bound = divisor / 2
        val epsilon = 0.001
        val halfX = entity.boundingBox.xLength / divisor
        val halfZ = entity.boundingBox.zLength / divisor
        var closest: Pair<BlockHitResult?, Double> = Pair(null, Double.MAX_VALUE)
        for (i in -bound..bound) {
            for (j in -bound..bound) {
                val corner = entityPos.add(
                    if (i == 0) 0.0 else halfX * i - epsilon,
                    0.0,
                    if (j == 0) 0.0 else halfZ * j - epsilon
                )
                val context = RaycastContext(
                    corner,
                    corner.add(0.0, -1.0, 0.0),
                    RaycastContext.ShapeType.COLLIDER,
                    RaycastContext.FluidHandling.NONE,
                    entity
                )
                val hit: BlockHitResult = world.raycast(context)
                if (hit.type == HitResult.Type.BLOCK) {
                    val dist = corner.subtract(hit.pos).y
                    if (dist < closest.component2()) {
                        closest = Pair(hit, dist)
                    }
                }
            }
        }
        val result = closest.component1()
        if (result != null) {
            val hitState: BlockState = world.getBlockState(result.blockPos)
            return Pair(result.blockPos, hitState)
        } else {
            return Pair(entity.blockPos.subtract(Vec3i(0, -1, 0)), Blocks.AIR.defaultState)
        }
    }

    @JvmStatic
    fun isLivingEntityWeak(livingEntity: LivingEntity?): Boolean {
        if (!XenoEarlyStartConfig.config.mobChanges.mobAttackWeakPlayer) {
            return false
        }
        if (livingEntity != null) {
            if (livingEntity.health <= 6) {
                return true
            } else if (livingEntity is PlayerEntity) {
                if (livingEntity.hungerManager.foodLevel <= 6) {
                    return true
                }
            }
        }
        return false
    }

    @JvmStatic
    fun moveEntityAwayFrom(
        entity: Entity,
        blockPos: Vec3d,
        blocks: Float
    ): Vec3d {
        // This is going to be very verbose because I have math skill issues, and I am stupid
        // Direction vector from block pos to entity
        val directionVector = entity.pos.subtract(blockPos).normalize()
        // Move the entity away from the block by `blocks`
        entity.setPosition(blockPos.add(directionVector.multiply(blocks.toDouble())))
        return directionVector
    }
}

