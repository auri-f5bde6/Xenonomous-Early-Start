package com.github.auri_f5bde6.xeno_early_start.event

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber

@Suppress("UNUSED_PARAMETER")
@EventBusSubscriber(modid = XenoEarlyStart.MODID, bus = EventBusSubscriber.Bus.FORGE)
object PlayerWakeUpEventHandler {
    @SubscribeEvent
    fun onPlayerWakeUpEvent(event: PlayerWakeUpEvent) {
        event.entity.hungerManager.addExhaustion(144f)
    }
}