package com.github.auri_f5bde6.xeno_early_start.entity.goal

import com.github.auri_f5bde6.xeno_early_start.capabilities.NeutralTilFedData
import net.minecraft.entity.mob.PathAwareEntity

class DomesticatableAttackGoal(mob: PathAwareEntity, val speed: Double) :
    net.minecraft.entity.ai.goal.MeleeAttackGoal(mob, speed, true) {
    override fun canStart(): Boolean {
        return !NeutralTilFedData.get(mob)!!.fed && super.canStart()
    }

    override fun canStop(): Boolean {
        if (NeutralTilFedData.get(mob)!!.fed) {
            stop()
            return true
        }
        return super.canStop()
    }
}