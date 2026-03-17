package com.github.auri_f5bde6.xeno_early_start.biome

import com.github.auri_f5bde6.xeno_early_start.config.XenoEarlyStartConfig
import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartCodecRegistry
import com.mojang.serialization.Codec
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.world.biome.Biome
import net.minecraftforge.common.world.BiomeModifier
import net.minecraftforge.common.world.ModifiableBiomeInfo

class ConfigBiomeModifier(val config: Type, val biomeModifier: BiomeModifier) : BiomeModifier {
    enum class Type(val literal: String) {
        NETHER_PHANTOM("nether_phantom"),
        END_PHANTOM("end_phantom"), ;

        companion object {
            fun fromString(type: String) = entries.find { v -> v.literal == type }
            val CODEC: Codec<Type> = Codec.STRING.xmap({ string ->
                val value =
                    fromString(string) ?: throw IllegalArgumentException("Invalid config type for ConfigBiomeModifier")
                value
            }, Type::literal)
        }
    }

    override fun modify(
        arg: RegistryEntry<Biome>,
        phase: BiomeModifier.Phase,
        builder: ModifiableBiomeInfo.BiomeInfo.Builder
    ) {
        val shouldApply = when (config) {
            Type.NETHER_PHANTOM -> XenoEarlyStartConfig.config.mobChanges.customPhantomNetherSpawn
            Type.END_PHANTOM -> XenoEarlyStartConfig.config.mobChanges.customPhantomEndSpawn
        }
        if (shouldApply) {
            biomeModifier.modify(arg, phase, builder)
        }
    }

    override fun codec(): Codec<ConfigBiomeModifier> {
        return XenoEarlyStartCodecRegistry.CONFIG_BIOME_MODIFIER.get()
    }
}