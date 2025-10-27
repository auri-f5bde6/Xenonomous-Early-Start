package net.hellomouse.xeno_early_start.utils

import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.registry.tag.BlockTags
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3i
import net.minecraft.world.BlockView
import net.minecraft.world.Heightmap
import net.minecraft.world.RaycastContext
import net.minecraft.world.World
import net.minecraftforge.common.Tags

object OtherUtils {
    /// Return (canSeeSky, isCovered)
    @JvmStatic
    fun rayCastToSky(world: World, pos: BlockPos): Pair<Boolean, Boolean> {
        val top = world.getTopY(Heightmap.Type.WORLD_SURFACE, pos.x, pos.z)
        if (top - 1 <= pos.y) {
            return Pair(true, false)
        }
        val start = Vec3d.of(pos.up())
        val end = Vec3d.of(pos.withY(top))
        if (start == end) {
            return Pair(false, false)
        }
        var isCovered = false
        val r = BlockView.raycast(
            start,
            end,
            null,
            { _, hitPos ->
                val blockState = world.getBlockState(hitPos)
                if (!blockState.isAir) {
                    isCovered = true
                }
                // TODO: I have 0 clues why raycasting from -60 to -58 can product a hit at -61 ...
                if (blockState.isAir || hitPos == pos || blockState.isIn(Tags.Blocks.GLASS) || blockState.isIn(BlockTags.LEAVES)) {
                    null
                } else {
                    false
                }
            },
            { _ -> true })
        return Pair(r!!, isCovered)
    }

    @JvmStatic
    fun getBlockAbove(world: World, pos: BlockPos): Pair<BlockPos, BlockState>? {
        val top = world.getTopY(Heightmap.Type.WORLD_SURFACE, pos.x, pos.z)
        val topBlockPos = pos.withY(top - 1)
        val topBlock = world.getBlockState(topBlockPos)
        if (top - 1 <= pos.y) {
            return null
        }
        return Pair(topBlockPos, topBlock)
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
}

