package com.github.auri_f5bde6.xeno_early_start.event

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.github.auri_f5bde6.xeno_early_start.entity.ProwlerEntity
import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartEntityRegistry
import net.minecraftforge.event.entity.EntityAttributeCreationEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber

@Suppress("UNUSED_PARAMETER")
@EventBusSubscriber(modid = XenoEarlyStart.MODID, bus = EventBusSubscriber.Bus.MOD)
object EntityAttributeCreationEventHandler {
    @SubscribeEvent
    fun create(event: EntityAttributeCreationEvent) {
        event.put(XenoEarlyStartEntityRegistry.PROWLER.get(), ProwlerEntity.createProwlerAttribute().build())
    }
}