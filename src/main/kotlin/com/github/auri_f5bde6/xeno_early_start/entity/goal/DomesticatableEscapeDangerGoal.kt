package com.github.auri_f5bde6.xeno_early_start.entity.goal

import com.github.auri_f5bde6.xeno_early_start.TillFedInterface
import net.minecraft.entity.mob.MobEntity

class DomesticatableEscapeDangerGoal(val mob: MobEntity) : net.minecraft.entity.ai.goal.AttackGoal(mob) {
    override fun canStart(): Boolean {
        val original = super.canStart()
        return mob.dataTracker.get((mob as TillFedInterface).`xeno_early_start$getFeedTrackedData`()) && original
    }
}