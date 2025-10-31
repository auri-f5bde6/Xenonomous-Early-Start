package net.hellomouse.xeno_early_start.registries

import net.hellomouse.xeno_early_start.ProgressionMod
import net.minecraft.particle.DefaultParticleType
import net.minecraft.particle.ParticleType
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject


class XenoProgressionModParticleRegistry {
    companion object {
        val DEF_REG: DeferredRegister<ParticleType<*>?> =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, ProgressionMod.MODID)

        @JvmField
        val COAL_DUST: RegistryObject<DefaultParticleType> =
            DEF_REG.register("coal_dust") { DefaultParticleType(false) }
    }
}