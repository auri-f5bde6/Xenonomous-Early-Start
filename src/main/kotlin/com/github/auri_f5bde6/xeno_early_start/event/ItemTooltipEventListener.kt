package com.github.auri_f5bde6.xeno_early_start.event

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStartConfig
import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStartTags
import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartItemRegistry
import net.minecraft.client.gui.screen.Screen
import net.minecraft.item.Items
import net.minecraft.text.MutableText
import net.minecraft.text.Style
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
            val t = Tooltips()
            if (event.itemStack.isOf(XenoEarlyStartItemRegistry.FIRE_STARTER.get())) {
                t.addItemDescriptionTooltip("fire_starter_purpose")
                t.addTutorialTooltip("fire_starter_description")
                t.addTutorialTooltip("fire_starter_chance")

            }
            if (event.itemStack.isOf(XenoEarlyStartItemRegistry.RAW_BRICK.get())) {
                t.addItemDescriptionTooltip("raw_brick")
            }
            if (event.itemStack.isOf(Items.BRICK)) {
                t.addTutorialTooltip("brick")
            }
            if (event.itemStack.isOf(XenoEarlyStartItemRegistry.PRIMITIVE_FIRE.get())) {
                t.addItemDescriptionTooltip("fire_starter_creation")
                t.addTutorialTooltip("primitive_fire1")
                t.addTutorialTooltip("primitive_fire2")
            }
            if (event.itemStack.isOf(XenoEarlyStartItemRegistry.PLANT_FIBER.get())) {
                t.addTutorialTooltip("plant_fiber")
            }
            if (event.itemStack.isIn(XenoEarlyStartTags.Items.PEBBLES)) {
                t.addTutorialTooltip("pebble")
                t.addItemDescriptionTooltip("pebble_to_shard")
            }
            if (event.itemStack.isIn(XenoEarlyStartTags.Items.SHARDS)) {
                t.addTutorialTooltip("shards")
            }
            if (event.itemStack.isIn(XenoEarlyStartTags.Items.KNAPPED_STONE)) {
                t.addTutorialTooltip("knapped_stone")
            }
            if (!XenoEarlyStartConfig.config.client.tooltips.disableFoodWarningTooltips) {
                if (event.itemStack.isIn(XenoEarlyStartTags.Items.RAW_FOOD_WARNING)) {
                    t.addToolTip("raw_food_warning")
                } else if (event.itemStack.isIn(XenoEarlyStartTags.Items.FOOD_WARNING)) {
                    t.addToolTip("food_warning")
                }
            }
            if (event.itemStack.isOf(Items.FLINT)) {
                t.addTutorialTooltip("flint")
            }
            if (event.itemStack.isOf(Items.WOODEN_PICKAXE)) {
                t.addTutorialTooltip("starting_guide")
            }
            t.insertTooltips(event, 1)

        }
    }

    private class Tooltips {
        var tooltips = arrayListOf<MutableText>()

        private fun getTranslatedText(name: String): MutableText {
            return Text.translatable(
                "xeno_early_start.item.tooltip.$name"
            )
        }

        fun addToolTip(name: String) {
            tooltips.add(
                getTranslatedText(name)
            )
        }

        fun addTutorialTooltip(name: String) {
            if (!XenoEarlyStartConfig.config.client.tooltips.disableTutorialTooltips) {
                addToolTip(name)
            }
        }

        fun addItemDescriptionTooltip(name: String) {
            if (!XenoEarlyStartConfig.config.client.tooltips.disableItemDescriptionTooltips) {
                addToolTip(name)
            }
        }

        fun insertTooltips(event: ItemTooltipEvent, at: Int) {
            if (tooltips.isEmpty()) {
                return
            }
            if (Screen.hasShiftDown() || tooltips.size == 1) {
                for (text in tooltips.reversed()) {
                    event.toolTip.add(at, text)
                }
            } else {
                event.toolTip.add(
                    at,
                    this.tooltips[0].append(
                        getTranslatedText("shift_hidden").setStyle(
                            Style.EMPTY.withItalic(true).withColor(0x3e3e3e)
                        )
                    )
                )
            }
        }
    }
}