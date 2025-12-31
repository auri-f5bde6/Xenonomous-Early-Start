package com.github.auri_f5bde6.xeno_early_start.entity.goal

import com.github.auri_f5bde6.xeno_early_start.TillFedInterface
import net.minecraft.entity.mob.PathAwareEntity

class DomesticatableRevengeGoal(mob: PathAwareEntity, vararg noRevengeTypes: Class<*>) :
    net.minecraft.entity.ai.goal.RevengeGoal(mob, *noRevengeTypes) {
    override fun canStart(): Boolean {
        return !mob.dataTracker.get((mob as TillFedInterface).`xeno_early_start$getFeedTrackedData`()) && super.canStart()
    }

    override fun canStop(): Boolean {
        return mob.dataTracker.get((mob as TillFedInterface).`xeno_early_start$getFeedTrackedData`()) || super.canStop()
    }
}