package com.github.auri_f5bde6.xeno_early_start.entity.goal

import com.github.auri_f5bde6.xeno_early_start.registries.ProgressionModBlockRegistry
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.WorldView

class WalkOnRawBrickGoal(mob: PathAwareEntity, speed: Double, maxYDifference: Int) :
    net.minecraft.entity.ai.goal.MoveToTargetPosGoal(mob, speed, 10, maxYDifference) {
    override fun isTargetPos(
        world: WorldView,
        pos: BlockPos
    ): Boolean {
        return world.getBlockState(pos).block === ProgressionModBlockRegistry.RAW_BRICK.get()
    }
}
