package net.hellomouse.xeno_early_start.utils

import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.Entity
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3i
import net.minecraft.world.Heightmap
import net.minecraft.world.RaycastContext
import net.minecraft.world.World
import net.minecraftforge.common.Tags

object OtherUtils {
    @JvmStatic
    fun canSeeSky(world: World, pos: BlockPos): Boolean {
        val top = world.getTopY(Heightmap.Type.WORLD_SURFACE, pos.x, pos.z)
        val topBlock = world.getBlockState(pos.withY(top - 1))
        return top - 1 <= pos.y || (!topBlock.isAir && topBlock.isIn(Tags.Blocks.GLASS))
    }

    @JvmStatic
    fun getBlockAbove(world: World, pos: BlockPos): BlockState? {
        val top = world.getTopY(Heightmap.Type.WORLD_SURFACE, pos.x, pos.z)
        val topBlock = world.getBlockState(pos.withY(top - 1))
        if (top - 1 <= pos.y) {
            return null
        }
        return topBlock
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
}

