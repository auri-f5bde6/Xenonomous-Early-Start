package net.hellomouse.xeno_early_start.registries

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.hellomouse.xeno_early_start.MultiplicativeSpawnModifier
import net.hellomouse.xeno_early_start.ProgressionMod
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryCodecs
import net.minecraftforge.common.world.BiomeModifier
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import java.util.function.Supplier


object XenoEarlyStartCodecRegistry {
    var DEF_REG: DeferredRegister<Codec<out BiomeModifier?>?> =
        DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, ProgressionMod.MODID)
    var MULTIPLICATIVE_SPAWN_MODIFIER: Supplier<Codec<MultiplicativeSpawnModifier>> =
        DEF_REG.register<Codec<MultiplicativeSpawnModifier>>("multiplicative_spawn_modifier") {
            RecordCodecBuilder.create { builder ->
                builder.group(
                    Codec.STRING.fieldOf("spawn_group").forGetter { it.spawnGroup },
                    RegistryCodecs.entryList(Registries.ENTITY_TYPE.key).fieldOf("entity_types")
                        .forGetter { it.entityTypeIds },
                    Codec.FLOAT.fieldOf("multiplier").forGetter { it.multiplier }
                ).apply(builder, ::MultiplicativeSpawnModifier)
            }
        }
}