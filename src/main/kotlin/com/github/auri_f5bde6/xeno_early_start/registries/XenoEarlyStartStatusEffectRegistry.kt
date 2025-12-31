package com.github.auri_f5bde6.xeno_early_start.registries

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.github.auri_f5bde6.xeno_early_start.entity.status_effect.FreezingStatusEffect
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectCategory
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object XenoEarlyStartStatusEffectRegistry {
    val DEF_REG: DeferredRegister<StatusEffect> =
        DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, XenoEarlyStart.MODID)

    @JvmField
    val FREEZING: RegistryObject<FreezingStatusEffect> = DEF_REG.register("freezing", {
        FreezingStatusEffect(StatusEffectCategory.HARMFUL, 0xF2FAFF)
    })
}