package com.github.auri_f5bde6.xeno_early_start.event

import com.github.auri_f5bde6.xeno_early_start.CoalDust
import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.github.auri_f5bde6.xeno_early_start.entity.CoalDustExplosion
import net.minecraft.server.world.ServerWorld
import net.minecraftforge.event.level.ExplosionEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber

@Suppress("UNUSED_PARAMETER")
@EventBusSubscriber(modid = XenoEarlyStart.MODID, bus = EventBusSubscriber.Bus.FORGE)
object ExplosionEventHandler {
    @SubscribeEvent
    fun onDetonate(event: ExplosionEvent.Detonate) {
        if (!event.level.isClient && event.explosion !is CoalDustExplosion) {
            for (i in event.affectedBlocks) {
                CoalDust.tryDetonate(event.level as ServerWorld, i, true, event.explosion.entity)
            }
        }
    }
}