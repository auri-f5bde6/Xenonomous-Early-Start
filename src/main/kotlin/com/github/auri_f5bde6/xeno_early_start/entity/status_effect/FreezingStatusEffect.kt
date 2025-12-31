package com.github.auri_f5bde6.xeno_early_start.entity.status_effect

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.AttributeContainer
import net.minecraft.entity.effect.StatusEffectCategory

class FreezingStatusEffect(category: StatusEffectCategory, color: Int) :
    net.minecraft.entity.effect.StatusEffect(category, color) {
    override fun onApplied(entity: LivingEntity, attributes: AttributeContainer, amplifier: Int) {
        super.onApplied(entity, attributes, amplifier)
        entity.setInPowderSnow(true)
    }

    override fun onRemoved(entity: LivingEntity, attributes: AttributeContainer, amplifier: Int) {
        super.onRemoved(entity, attributes, amplifier)
        entity.setInPowderSnow(false)
    }

    override fun applyUpdateEffect(entity: LivingEntity, amplifier: Int) {
        super.applyUpdateEffect(entity, amplifier)
        entity.setInPowderSnow(true)
    }

    override fun canApplyUpdateEffect(duration: Int, amplifier: Int): Boolean {
        return true
    }
}