package net.hellomouse.xeno_early_start

import com.mojang.serialization.Codec
import net.hellomouse.xeno_early_start.registries.XenoEarlyStartCodecRegistry
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.entry.RegistryEntryList
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.SpawnSettings
import net.minecraftforge.common.world.BiomeModifier
import net.minecraftforge.common.world.ModifiableBiomeInfo

@JvmRecord
data class MultiplicativeSpawnModifier(
    val spawnGroup: String,
    val entityTypeIds: RegistryEntryList<EntityType<*>>,
    val multiplier: Float
) :
    BiomeModifier {
    override fun modify(
        arg: RegistryEntry<Biome>,
        phase: BiomeModifier.Phase,
        builder: ModifiableBiomeInfo.BiomeInfo.Builder
    ) {
        if (phase == BiomeModifier.Phase.MODIFY) {
            val spawnGroup = SpawnGroup.byName(this.spawnGroup)
            if (spawnGroup == null) {
                ProgressionMod.LOGGER.error("No spawn group found for $spawnGroup, skipping")
                return
            }
            val spawners = builder.mobSpawnSettings.getSpawner(spawnGroup)
            val remove = ArrayList<SpawnSettings.SpawnEntry>()
            val add = ArrayList<Pair<SpawnGroup, SpawnSettings.SpawnEntry>>()
            for (spawner in spawners) {
                val entry = Registries.ENTITY_TYPE.getEntry(spawner.type)
                if (entityTypeIds.contains(entry)) {
                    remove.add(spawner)
                    val originalWeight = spawner.weight.value
                    val newWeight = ((originalWeight * multiplier).toInt()).coerceAtLeast(1)
                    add.add(
                        Pair(
                            spawnGroup,
                            SpawnSettings.SpawnEntry(
                                spawner.type,
                                newWeight as Int,
                                spawner.minGroupSize,
                                spawner.maxGroupSize
                            )
                        )
                    )
                }
            }
            for (v in remove) {
                spawners.remove(v)
            }
            for (a in add) {
                builder.mobSpawnSettings.spawn(a.first, a.second)
            }
        }
    }

    override fun codec(): Codec<out BiomeModifier> {
        return XenoEarlyStartCodecRegistry.MULTIPLICATIVE_SPAWN_MODIFIER.get()
    }
}