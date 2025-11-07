package net.hellomouse.xeno_early_start.registries

import net.hellomouse.xeno_early_start.ProgressionMod
import net.hellomouse.xeno_early_start.entity.status_effect.FreezingStatusEffect
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectCategory
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object XenoEarlyStartStatusEffectRegistry {
    val DEF_REG: DeferredRegister<StatusEffect> =
        DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ProgressionMod.MODID)

    @JvmField
    val FREEZING: RegistryObject<FreezingStatusEffect> = DEF_REG.register("freezing", {
        FreezingStatusEffect(StatusEffectCategory.HARMFUL, 0xF2FAFF)
    })
}