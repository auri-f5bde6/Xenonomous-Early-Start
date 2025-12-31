package com.github.auri_f5bde6.xeno_early_start.event

import com.github.auri_f5bde6.xeno_early_start.ProgressionMod
import net.minecraft.block.Blocks
import net.minecraftforge.event.level.BlockEvent
import net.minecraftforge.eventbus.api.Event
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber

@Suppress("UNUSED_PARAMETER")
@EventBusSubscriber(modid = ProgressionMod.MODID, bus = EventBusSubscriber.Bus.FORGE)
object CropGrowEventHandler {
    @SubscribeEvent
    fun cropGrowPre(event: BlockEvent.CropGrowEvent.Pre) {
        if (event.state.isOf(Blocks.SWEET_BERRY_BUSH)) {
            event.result = if (event.level.random.nextFloat() > 0.5) Event.Result.DENY else Event.Result.DEFAULT
        }
    }
}