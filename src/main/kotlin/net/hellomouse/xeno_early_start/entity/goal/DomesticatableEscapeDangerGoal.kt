package net.hellomouse.xeno_early_start.entity.goal

import net.hellomouse.xeno_early_start.TillFedInterface
import net.minecraft.entity.ai.goal.AttackGoal
import net.minecraft.entity.mob.MobEntity

class DomesticatableEscapeDangerGoal(val mob: MobEntity) : AttackGoal(mob) {
    override fun canStart(): Boolean {
        val original = super.canStart()
        return mob.dataTracker.get((mob as TillFedInterface).`xeno_early_start$getFeedTrackedData`()) && original
    }
}