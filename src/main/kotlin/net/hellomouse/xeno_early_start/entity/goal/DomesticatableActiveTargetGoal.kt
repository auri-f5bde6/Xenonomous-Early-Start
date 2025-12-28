package net.hellomouse.xeno_early_start.entity.goal

import net.hellomouse.xeno_early_start.TillFedInterface
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.goal.ActiveTargetGoal
import net.minecraft.entity.mob.MobEntity
import java.util.function.Predicate

class DomesticatableActiveTargetGoal<T : LivingEntity>(
    mob: MobEntity,
    targetClass: Class<T>,
    reciprocalChance: Int,
    checkVisibility: Boolean,
    checkCanNavigate: Boolean,
    targetPredicate: Predicate<LivingEntity>
) : ActiveTargetGoal<T>(
    mob, targetClass, reciprocalChance,
    checkVisibility, checkCanNavigate, targetPredicate
) {
    override fun canStart(): Boolean {
        if (mob.dataTracker.get((mob as TillFedInterface).`xeno_early_start$getFeedTrackedData`())) {
            return false
        }
        return super.canStart()
    }

    override fun canStop(): Boolean {
        return mob.dataTracker.get((mob as TillFedInterface).`xeno_early_start$getFeedTrackedData`()) || super.canStop()
    }
}