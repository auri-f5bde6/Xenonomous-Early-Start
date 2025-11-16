package net.hellomouse.xeno_early_start.event

import net.hellomouse.xeno_early_start.ProgressionMod
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber

@Suppress("UNUSED_PARAMETER")
@EventBusSubscriber(modid = ProgressionMod.MODID, bus = EventBusSubscriber.Bus.FORGE)
object PlayerWakeUpEventHandler {
    @SubscribeEvent
    fun onPlayerWakeUpEvent(event: PlayerWakeUpEvent) {
        event.entity.hungerManager.addExhaustion(144f)
    }
}