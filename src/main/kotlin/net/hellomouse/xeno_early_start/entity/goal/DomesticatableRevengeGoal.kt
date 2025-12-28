package net.hellomouse.xeno_early_start.entity.goal

import net.hellomouse.xeno_early_start.TillFedInterface
import net.minecraft.entity.ai.goal.RevengeGoal
import net.minecraft.entity.mob.PathAwareEntity

class DomesticatableRevengeGoal(mob: PathAwareEntity, vararg noRevengeTypes: Class<*>) :
    RevengeGoal(mob, *noRevengeTypes) {
    override fun canStart(): Boolean {
        return !mob.dataTracker.get((mob as TillFedInterface).`xeno_early_start$getFeedTrackedData`()) && super.canStart()
    }

    override fun canStop(): Boolean {
        return mob.dataTracker.get((mob as TillFedInterface).`xeno_early_start$getFeedTrackedData`()) || super.canStop()
    }
}