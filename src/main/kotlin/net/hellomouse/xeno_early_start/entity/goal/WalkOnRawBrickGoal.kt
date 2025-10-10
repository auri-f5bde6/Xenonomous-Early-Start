package net.hellomouse.xeno_early_start.entity.goal

import net.hellomouse.xeno_early_start.registries.ProgressionModBlockRegistry
import net.minecraft.entity.ai.goal.MoveToTargetPosGoal
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.WorldView

class WalkOnRawBrickGoal(mob: PathAwareEntity, speed: Double, maxYDifference: Int) :
    MoveToTargetPosGoal(mob, speed, 10, maxYDifference) {
    override fun isTargetPos(
        world: WorldView,
        pos: BlockPos
    ): Boolean {
        return world.getBlockState(pos).block === ProgressionModBlockRegistry.RAW_BRICK.get()
    }
}
