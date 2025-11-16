package net.hellomouse.xeno_early_start.event

import net.hellomouse.xeno_early_start.ProgressionMod
import net.hellomouse.xeno_early_start.registries.ProgressionModItemRegistry
import net.minecraft.text.Text
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber

@Suppress("UNUSED_PARAMETER")
@EventBusSubscriber(modid = ProgressionMod.MODID, bus = EventBusSubscriber.Bus.FORGE)
object ItemTooltipEventListener {
    @SubscribeEvent
    fun onItemTooltipEvent(event: ItemTooltipEvent) {
        if (event.itemStack.isOf(ProgressionModItemRegistry.FIRE_STARTER.get())) {
            event.toolTip.add(1, Text.translatable("xeno_early_start.item.tooltip.fire_starter"))
        }
    }
}