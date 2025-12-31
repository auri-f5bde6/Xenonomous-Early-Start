package com.github.auri_f5bde6.xeno_early_start.registries

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import net.minecraft.particle.DefaultParticleType
import net.minecraft.particle.ParticleType
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject


object XenoEarlyStartParticleRegistry {
    val DEF_REG: DeferredRegister<ParticleType<*>?> =
        DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, XenoEarlyStart.MODID)

    @JvmField
    val COAL_DUST: RegistryObject<DefaultParticleType> =
        DEF_REG.register("coal_dust") { DefaultParticleType(false) }
}