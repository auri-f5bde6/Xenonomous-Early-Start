package net.hellomouse.xeno_early_start.utils

import net.minecraft.util.math.BlockPos
import net.minecraft.world.Heightmap
import net.minecraft.world.World

object OtherUtils {
    fun canSeeSky(world: World, pos: BlockPos): Boolean {
        return world.getTopY(Heightmap.Type.WORLD_SURFACE, pos.x, pos.z)-1 <= pos.y
    }
}

