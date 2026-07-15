package com.github.auri_f5bde6.xeno_early_start.registries

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import net.minecraft.sound.SoundEvent
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject


object XenoEarlyStartSoundEventRegistry {
    val DEF_REG: DeferredRegister<SoundEvent> =
        DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, XenoEarlyStart.MODID)
    val DUST_TURNS_TO_CLAY: RegistryObject<SoundEvent> =
        DEF_REG.register("dust_turns_to_clay") { SoundEvent.of(XenoEarlyStart.of("dust_turns_to_clay")) }
}