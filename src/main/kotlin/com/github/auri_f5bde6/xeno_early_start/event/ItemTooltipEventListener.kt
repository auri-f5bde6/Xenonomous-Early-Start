package com.github.auri_f5bde6.xeno_early_start.event

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStartConfig
import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStartTags
import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartItemRegistry
import net.minecraft.item.Items
import net.minecraft.text.Text
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber

@Suppress("UNUSED_PARAMETER")
@EventBusSubscriber(modid = XenoEarlyStart.MODID, bus = EventBusSubscriber.Bus.FORGE)
object ItemTooltipEventListener {
    @SubscribeEvent
    fun onItemTooltipEvent(event: ItemTooltipEvent) {
        if (!XenoEarlyStartConfig.config.client.tooltips.disableAllTooltips) {
            if (event.itemStack.isOf(XenoEarlyStartItemRegistry.FIRE_STARTER.get())) {
                var i = addItemDescriptionTooltip(event, 1, "fire_starter_purpose")
                i = addTutorialTooltip(event, i, "fire_starter_description")
                addTutorialTooltip(event, i, "fire_starter_chance")
            }
            if (event.itemStack.isOf(XenoEarlyStartItemRegistry.RAW_BRICK.get())) {
                addItemDescriptionTooltip(event, 1, "raw_brick")
            }
            if (event.itemStack.isOf(Items.BRICK)) {
                addTutorialTooltip(event, 1, "brick")
            }
            if (event.itemStack.isOf(XenoEarlyStartItemRegistry.PRIMITIVE_FIRE.get())) {
                var i = addItemDescriptionTooltip(event, 1, "fire_starter_creation")
                i = addTutorialTooltip(event, i, "primitive_fire1")
                addTutorialTooltip(event, i, "primitive_fire2")
            }
            if (event.itemStack.isOf(XenoEarlyStartItemRegistry.PLANT_FIBER.get())) {
                addTutorialTooltip(event, 1, "plant_fiber")
            }
            if (event.itemStack.isIn(XenoEarlyStartTags.Items.PEBBLES)) {
                val i = addTutorialTooltip(event, 1, "pebble")
                addItemDescriptionTooltip(event, i, "pebble_to_shard")

            }
            if (event.itemStack.isIn(XenoEarlyStartTags.Items.SHARDS)) {
                addTutorialTooltip(event, 1, "shards")
            }
            if (event.itemStack.isIn(XenoEarlyStartTags.Items.KNAPPED_STONE)) {
                addTutorialTooltip(event, 1, "knapped_stone")
            }
            if (!XenoEarlyStartConfig.config.client.tooltips.disableFoodWarningTooltips) {
                if (event.itemStack.isIn(XenoEarlyStartTags.Items.RAW_FOOD_WARNING)) {
                    addToolTip(event, 1, "raw_food_warning")
                } else if (event.itemStack.isIn(XenoEarlyStartTags.Items.FOOD_WARNING)) {
                    addToolTip(event, 1, "food_warning")
                }
            }
            if (event.itemStack.isOf(Items.FLINT)) {
                addTutorialTooltip(event, 1, "flint")
            }
        }
    }

    private fun addToolTip(event: ItemTooltipEvent, index: Int, name: String): Int {
        event.toolTip.add(
            index,
            Text.translatable(
                "xeno_early_start.item.tooltip.$name"
            )
        )
        return index + 1
    }

    private fun addTutorialTooltip(event: ItemTooltipEvent, index: Int, name: String): Int {
        if (!XenoEarlyStartConfig.config.client.tooltips.disableTutorialTooltips) {
            return addToolTip(event, index, name)
        }
        return index
    }

    private fun addItemDescriptionTooltip(event: ItemTooltipEvent, index: Int, name: String): Int {
        if (!XenoEarlyStartConfig.config.client.tooltips.disableItemDescriptionTooltips) {
            return addToolTip(event, index, name)
        }
        return index
    }
}