package com.github.auri_f5bde6.xeno_early_start.event

import com.github.auri_f5bde6.xeno_early_start.ProgressionMod
import com.github.auri_f5bde6.xeno_early_start.registries.ProgressionModItemRegistry
import net.minecraft.item.Items
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
        } else if (event.itemStack.isOf(ProgressionModItemRegistry.RAW_BRICK.get())) {
            event.toolTip.add(1, Text.translatable("xeno_early_start.item.tooltip.raw_brick"))
        } else if (event.itemStack.isOf(Items.BRICK)) {
            event.toolTip.add(1, Text.translatable("xeno_early_start.item.tooltip.brick"))
        } else if (event.itemStack.isOf(ProgressionModItemRegistry.PRIMITIVE_FIRE.get())) {
            event.toolTip.add(1, Text.translatable("xeno_early_start.item.tooltip.primitive_fire1"))
            event.toolTip.add(2, Text.translatable("xeno_early_start.item.tooltip.primitive_fire2"))

        }
    }
}