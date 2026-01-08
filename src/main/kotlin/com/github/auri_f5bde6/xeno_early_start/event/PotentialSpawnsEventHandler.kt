package com.github.auri_f5bde6.xeno_early_start.event

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import net.minecraft.entity.EntityType
import net.minecraft.server.world.ServerWorld
import net.minecraft.world.SpawnHelper.shouldUseNetherFortressSpawns
import net.minecraft.world.biome.SpawnSettings
import net.minecraftforge.event.level.LevelEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber

@Suppress("UNUSED_PARAMETER")
@EventBusSubscriber(modid = XenoEarlyStart.MODID, bus = EventBusSubscriber.Bus.FORGE)
object PotentialSpawnsEventHandler {

    // See: net.minecraft.world.SpawnHelper.spawnEntitiesInChunk
    // The spawn entry returned by net.minecraft.world.MobSpawnerLogic.getSpawnEntry must be the exact same instance as the later returned in net.minecraft.world.SpawnHelper.containsSpawnEntry
    // A bit janky but it works (alternatively I could've mixined containsSpawnEntry to compare the attribute instead of using Object.equal, but im refraining from modifying vanilla behavior)
    var spawnEntry: SpawnSettings.SpawnEntry? = null

    @SubscribeEvent
    fun getPotentialSpawn(event: LevelEvent.PotentialSpawns) {
        val level = event.level
        if (level is ServerWorld && shouldUseNetherFortressSpawns(
                event.pos,
                level,
                event.mobCategory,
                level.structureAccessor
            )
        ) {
            var removedEntries = mutableListOf<SpawnSettings.SpawnEntry>()
            var witherSkeletonEntry: SpawnSettings.SpawnEntry? = null
            var addWeight = 100
            for (i in event.spawnerDataList) {
                when (i.type) {
                    EntityType.SKELETON -> {
                        removedEntries.add(i)
                        addWeight += i.weight.value
                    }

                    EntityType.WITHER_SKELETON -> {
                        if (i.weight.value > (witherSkeletonEntry?.weight?.value ?: 0)) {
                            witherSkeletonEntry = i
                        }
                    }
                }
            }

            if (witherSkeletonEntry != null) {
                for (entry in removedEntries) {
                    event.removeSpawnerData(entry)
                }
                event.removeSpawnerData(witherSkeletonEntry)
                if (spawnEntry == null || !(witherSkeletonEntry.type == spawnEntry?.type && witherSkeletonEntry.weight.value + addWeight == spawnEntry?.weight?.value && witherSkeletonEntry.minGroupSize == spawnEntry?.minGroupSize && witherSkeletonEntry.maxGroupSize == spawnEntry?.maxGroupSize)) {
                    spawnEntry = SpawnSettings.SpawnEntry(
                        witherSkeletonEntry.type,
                        witherSkeletonEntry.weight.value + addWeight,
                        witherSkeletonEntry.minGroupSize,
                        witherSkeletonEntry.maxGroupSize
                    )
                }
                event.addSpawnerData(
                    spawnEntry
                )
            }
        }
    }
}