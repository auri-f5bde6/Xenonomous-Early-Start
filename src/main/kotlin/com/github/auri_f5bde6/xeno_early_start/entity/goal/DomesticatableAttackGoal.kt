package com.github.auri_f5bde6.xeno_early_start.entity.goal

import com.github.auri_f5bde6.xeno_early_start.TillFedInterface
import net.minecraft.entity.mob.MobEntity

class DomesticatableAttackGoal(val mob: MobEntity) : net.minecraft.entity.ai.goal.AttackGoal(mob) {
    override fun canStart(): Boolean {
        return !mob.dataTracker.get((mob as TillFedInterface).`xeno_early_start$getFeedTrackedData`()) && super.canStart()
    }

    override fun canStop(): Boolean {
        return mob.dataTracker.get((mob as TillFedInterface).`xeno_early_start$getFeedTrackedData`()) || super.canStop()
    }
}