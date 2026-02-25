package com.github.auri_f5bde6.xeno_early_start.advancements

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import net.minecraft.advancement.criterion.Criteria
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent

@EventBusSubscriber(modid = XenoEarlyStart.MODID, bus = EventBusSubscriber.Bus.MOD)
object XenoEarlyStartCriteria {
    val BREAK_BLOCK = BreakBlockCriterion()
    val PRIMITIVE_FIRE_CREATION = PrimitiveFireCriterion()

    @SubscribeEvent
    fun commonSetup(e: FMLCommonSetupEvent) {
        e.enqueueWork {
            Criteria.register(BREAK_BLOCK)
            Criteria.register(PRIMITIVE_FIRE_CREATION)
        }
    }
}