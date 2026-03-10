package com.github.auri_f5bde6.xeno_early_start.entity.goal

import net.minecraft.entity.ai.NoPenaltyTargeting
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.Vec3d
import java.util.function.Supplier

class RunawayFromPlayerGoal(val mob: PathAwareEntity, val speed: Double, val beenFed: Supplier<Boolean>) :
    net.minecraft.entity.ai.goal.Goal() {
    var player: PlayerEntity? = null
    var target: Vec3d? = null
    override fun canStart(): Boolean {
        if (beenFed.get()) {
            return false
        }
        val currentPos = mob.blockPos
        player = mob.world.getClosestPlayer(
            currentPos.x.toDouble(),
            currentPos.y.toDouble(),
            currentPos.z.toDouble(),
            32.0,
            true
        )
        if (player != null) {
            return findTarget()
        }
        return false
    }

    override fun start() {
        this.mob.getNavigation().startMovingTo(target!!.x, target!!.y, target!!.z, speed)
    }

    override fun canStop(): Boolean {
        return beenFed.get() || (this.mob.squaredDistanceTo(player) >= 32.0 || player?.isAlive == false)
    }

    private fun findTarget(): Boolean {
        val vec3d = NoPenaltyTargeting.findFrom(this.mob, 10, 4, player!!.pos)
        if (vec3d == null) {
            return false
        } else {
            this.target = vec3d
            return true
        }
    }


    override fun stop() {
        this.player = null
    }

    override fun tick() {
        if (this.mob.getNavigation().isIdle) {
            findTarget()
            this.mob.getNavigation().startMovingTo(target!!.x, target!!.y, target!!.z, speed)
        }
        if (beenFed.get()) {
            this.mob.navigation.stop()
        }
    }
}