package com.github.auri_f5bde6.xeno_early_start.entity.goal

import net.minecraft.entity.ai.goal.BreakDoorGoal
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.passive.PolarBearEntity
import net.minecraft.world.Difficulty
import java.util.function.Predicate

class NonZombieBreakDoorGoal(
    mob: MobEntity,
    difficultyPredicate: Predicate<Difficulty>,
    maxProgress: Int
) : BreakDoorGoal(
    mob,
    maxProgress,
    difficultyPredicate
) {
    override fun getMaxProgress(): Int {
        return maxProgress
    }

    override fun tick() {
        super.tick()
        val mob = this.mob
        if (mob is PolarBearEntity) {
            if (breakProgress.mod(40) > 20) {
                mob.isWarning = !mob.isWarning
            }
        }
    }

    override fun isDoorOpen(): Boolean {
        return false
    }
}