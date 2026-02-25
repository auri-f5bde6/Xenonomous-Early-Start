package com.github.auri_f5bde6.xeno_early_start.event

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import net.minecraftforge.event.entity.EntityJoinLevelEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber

@EventBusSubscriber(modid = XenoEarlyStart.MODID, bus = EventBusSubscriber.Bus.FORGE)
class EntityJoinLevelEventHandler {
    @SubscribeEvent
    fun onEntityJoinLevel(event: EntityJoinLevelEvent) {
        /*val e=event.entity
        if (e is PlayerEntity) {
            e.giveItemStack(XenoEarlyStartItemRegistry.GUIDE.get().defaultStack)
        }*/
    }
}