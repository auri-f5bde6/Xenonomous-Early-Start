package net.hellomouse.xeno_early_start.utils

import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.Heightmap
import net.minecraft.world.RaycastContext
import net.minecraft.world.World

object OtherUtils {
    @JvmStatic
    fun canSeeSky(world: World, pos: BlockPos): Boolean {
        return world.getTopY(Heightmap.Type.WORLD_SURFACE, pos.x, pos.z) - 1 <= pos.y
    }

    @JvmStatic
    fun raycastSteppingOn(world: World, entityPos: Vec3d, entity: Entity): Pair<BlockHitResult, BlockState> {
        val context = RaycastContext(
            entityPos,
            entityPos.add(0.0, -1.0, 0.0),
            RaycastContext.ShapeType.OUTLINE,
            RaycastContext.FluidHandling.NONE,
            entity
        )
        val hit: BlockHitResult = world.raycast(context)
        val hitState: BlockState = world.getBlockState(hit.blockPos)
        return Pair(hit, hitState)
    }
}

