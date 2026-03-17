package com.github.auri_f5bde6.xeno_early_start.entity.goal

import net.minecraft.entity.ai.goal.Goal
import net.minecraft.entity.ai.pathing.MobNavigation
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraftforge.common.Tags

class BreakGlassGoal(val mob: PathAwareEntity, val shouldStart: () -> Boolean) : Goal() {
    var shouldContinue = false
    var elapsedTicks: Int = 0

    override fun canStart(): Boolean {
        if (!shouldStart()) {
            return false
        }
        val mobNavigation = this.mob.getNavigation() as MobNavigation
        val path = mobNavigation.getCurrentPath()
        if (path != null && !path.isFinished) {
            for (i in -2..2) {
                for (j in -2..2) {
                    for (k in -2..2) {
                        if (mob.world.getBlockState(mob.blockPos.add(i, j, k)).isIn(Tags.Blocks.GLASS)) {
                            return true
                        }
                    }
                }
            }
        }
        return false
    }

    override fun shouldRunEveryTick(): Boolean {
        return true
    }

    override fun start() {
        shouldContinue = true
        elapsedTicks = 0
    }

    override fun shouldContinue(): Boolean {
        return shouldContinue
    }

    override fun tick() {
        for (i in -2..2) {
            for (j in -2..2) {
                for (k in -2..2) {
                    val pos = mob.blockPos.add(i, j, k)
                    if (mob.world.getBlockState(pos).isIn(Tags.Blocks.GLASS)) {
                        val progress = elapsedTicks.toFloat() / 25.0 * 10
                        mob.world.setBlockBreakingInfo(mob.id, pos, progress.toInt())
                        if (progress >= 10) {
                            mob.world.breakBlock(pos, false, mob)
                            shouldContinue = false
                        }
                    }
                }
            }
        }
        elapsedTicks++
    }
}