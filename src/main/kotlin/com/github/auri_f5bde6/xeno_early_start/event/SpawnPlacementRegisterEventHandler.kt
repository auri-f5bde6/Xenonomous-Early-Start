package com.github.auri_f5bde6.xeno_early_start.event

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.github.auri_f5bde6.xeno_early_start.entity.ProwlerEntity
import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartEntityRegistry
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.SpawnRestriction
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.world.Heightmap
import net.minecraft.world.ServerWorldAccess
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber

@Suppress("UNUSED_PARAMETER")
@EventBusSubscriber(modid = XenoEarlyStart.MODID, bus = EventBusSubscriber.Bus.FORGE)
object SpawnPlacementRegisterEventHandler {
    @SubscribeEvent
    fun register(event: SpawnPlacementRegisterEvent) {
        event.register(
            XenoEarlyStartEntityRegistry.PROWLER.get(),
            SpawnRestriction.Location.ON_GROUND,
            Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
            { type: EntityType<ProwlerEntity>, world: ServerWorldAccess, spawnReason: SpawnReason, pos: BlockPos, random: Random ->
                HostileEntity.canSpawnInDark(
                    type,
                    world,
                    spawnReason,
                    pos,
                    random
                ) && ProwlerEntity.canSpawn(type, world, spawnReason, pos, random)
            }, SpawnPlacementRegisterEvent.Operation.AND
        )

    }
}