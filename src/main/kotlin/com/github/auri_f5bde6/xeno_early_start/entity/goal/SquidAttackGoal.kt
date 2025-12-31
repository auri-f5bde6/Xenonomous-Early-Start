package com.github.auri_f5bde6.xeno_early_start.entity.goal

import com.github.auri_f5bde6.xeno_early_start.mixins.accessors.MeleeAttackGoalAccessor
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.goal.MeleeAttackGoal
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.passive.SquidEntity
import java.lang.StrictMath.pow
import kotlin.math.ln
import kotlin.math.max

class SquidAttackGoal(entity: SquidEntity) : MeleeAttackGoal(entity, 10.0, true) {
    val range = pow(this.mob.getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE), 2.0)
    override fun getSquaredMaxAttackDistance(entity: LivingEntity?): Double {
        return 9.0
    }

    override fun attack(target: LivingEntity, squaredDistance: Double) {
        if (squaredDistance <= 1.5) {
            super.attack(target, squaredDistance)
        }
        if (squaredDistance <= range) {
            val directionVector = this.mob.pos.subtract(target.pos).normalize()
            target.addVelocity(directionVector.multiply(ln(squaredDistance + 1.0) / 2.0))
        }
    }

    override fun tick() {
        if (this.mob.target != null) {
            val distSquared = this.mob.getSquaredDistanceToAttackPosOf(this.mob.target)
            val target: LivingEntity = this.mob.target ?: return
            this.mob.lookAtEntity(target, 30.0f, 30.0f)
            val velocity = target.pos.subtract(this.mob.pos).normalize().multiply(0.35).toVector3f()
            (this.mob as SquidEntity).setSwimmingVector(
                velocity.x,
                velocity.y,
                velocity.z
            )
            (this as MeleeAttackGoal as MeleeAttackGoalAccessor).`xeno_early_start$setCooldown`(
                max(
                    this.cooldown - 1,
                    0
                )
            )
            this.attack(target, distSquared)
        }
    }

}