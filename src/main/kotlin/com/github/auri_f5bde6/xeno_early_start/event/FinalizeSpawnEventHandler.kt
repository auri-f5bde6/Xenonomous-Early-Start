package com.github.auri_f5bde6.xeno_early_start.event

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.github.auri_f5bde6.xeno_early_start.mixins.accessors.PhantomEntityAccessor
import com.github.auri_f5bde6.xeno_early_start.utils.OtherUtils
import net.minecraft.entity.EntityType
import net.minecraft.entity.mob.PhantomEntity
import net.minecraft.util.math.Direction
import net.minecraft.world.SpawnHelper
import net.minecraftforge.event.entity.living.MobSpawnEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import kotlin.math.max

@Suppress("UNUSED")
@EventBusSubscriber(modid = XenoEarlyStart.MODID, bus = EventBusSubscriber.Bus.FORGE)
object FinalizeSpawnEventHandler {

    // p.s. this event doesn't get fired in dev environment, for some reason
    @SubscribeEvent
    fun onFinalizeSpawn(event: MobSpawnEvent.FinalizeSpawn) {
        val entity = event.entity
        if (entity is PhantomEntity) {
            val world = entity.world
            val random = world.random
            val result = OtherUtils.raycast(world, entity.blockPos, Direction.DOWN, 10) { false }
            if (result != null) {
                var spawnPos = result.second
                val top = OtherUtils.raycast(world, spawnPos, Direction.UP, 35) { true }

                if (top != null) {
                    val height = top.second.y - spawnPos.y
                    spawnPos = spawnPos.withY(spawnPos.y + 20 + random.nextInt(max(0, height - 20)))
                } else {
                    spawnPos = spawnPos.withY(20 + random.nextInt(15))
                }

                if (SpawnHelper.isClearForSpawn(
                        world,
                        spawnPos,
                        world.getBlockState(spawnPos),
                        world.getFluidState(spawnPos),
                        EntityType.PHANTOM
                    )
                ) {
                    (entity as PhantomEntityAccessor).circlingCenter = spawnPos
                    entity.setPosition(entity.x, spawnPos.y.toDouble(), entity.z)
                }
            }
        }
    }
}