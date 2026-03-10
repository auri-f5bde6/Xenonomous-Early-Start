package com.github.auri_f5bde6.xeno_early_start.entity.goal

import com.github.auri_f5bde6.xeno_early_start.capabilities.NeutralTilFedData
import net.minecraft.entity.mob.MobEntity

class DomesticatableAttackGoal(val mob: MobEntity) : net.minecraft.entity.ai.goal.AttackGoal(mob) {
    override fun canStart(): Boolean {
        return !NeutralTilFedData.get(mob)!!.hasBeenFed && super.canStart()
    }

    override fun canStop(): Boolean {
        return NeutralTilFedData.get(mob)!!.hasBeenFed || super.canStop()
    }
}