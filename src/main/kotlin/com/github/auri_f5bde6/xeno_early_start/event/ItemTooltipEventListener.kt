package com.github.auri_f5bde6.xeno_early_start.event

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStartConfig
import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStartTags
import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartItemRegistry
import net.minecraft.client.gui.screen.Screen
import net.minecraft.item.Items
import net.minecraft.recipe.RecipeType
import net.minecraft.text.MutableText
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.Language
import net.minecraftforge.common.ForgeHooks.getBurnTime
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import kotlin.math.ceil

@Suppress("UNUSED_PARAMETER")
@EventBusSubscriber(modid = XenoEarlyStart.MODID, bus = EventBusSubscriber.Bus.FORGE)
object ItemTooltipEventListener {
    fun calculateNumberOfStickRequired(fuelTime: Float): Int {
        val stickBurnTime = getBurnTime(Items.STICK.defaultStack, RecipeType.SMELTING).toFloat()
        return ceil(fuelTime / (stickBurnTime * XenoEarlyStartConfig.config.earlyGameChanges.primitiveFire.fuelTimeMultiplier)).toInt()
    }

    val AURI: Style = Style.EMPTY.withColor(0xf5bde6) // :3
    fun auriText(string: String, style: Style = AURI): MutableText {
        return Text.literal(string).setStyle(style)
    }

    @SubscribeEvent
    fun onItemTooltipEvent(event: ItemTooltipEvent) {
        if (!XenoEarlyStartConfig.config.client.tooltips.disableAllTooltips) {
            val t = Tooltips()
            val primitiveFire = XenoEarlyStartConfig.config.earlyGameChanges.primitiveFire
            if (event.itemStack.isOf(XenoEarlyStartItemRegistry.FIRE_STARTER.get())) {
                t.addItemDescriptionTooltip(
                    "fire_starter_purpose",
                    "${primitiveFire.fuelStarterRelightFuelTime / 60 / 20}"
                )
                t.addTutorialTooltip(
                    "fire_starter_tutorial",
                    "${primitiveFire.maxBurnTime / 20 / 60}",
                    "${calculateNumberOfStickRequired(primitiveFire.percentageRequiredForMaxBrightness * primitiveFire.maxBurnTime)}"
                )
                t.addTutorialTooltip("fire_starter_chance")

            }
            if (event.itemStack.isOf(XenoEarlyStartItemRegistry.RAW_BRICK.get())) {
                t.addItemDescriptionTooltip("raw_brick")
                t.addTutorialTooltip("raw_brick_warning")
                t.addTutorialTooltip("raw_brick_zombie")
            }
            if (event.itemStack.isOf(Items.BRICK)) {
                t.addItemDescriptionTooltip("brick_throwable")
                t.addTutorialTooltip("brick_obtain")
            }
            if (event.itemStack.isOf(XenoEarlyStartItemRegistry.PRIMITIVE_FIRE.get())) {
                t.addItemDescriptionTooltip("fire_starter_creation")
                t.addTutorialTooltip("primitive_fire1")
                t.addTutorialTooltip(
                    "primitive_fire2",
                    "${primitiveFire.maxBurnTime / 20 / 60}",
                    "${calculateNumberOfStickRequired(primitiveFire.maxBurnTime.toFloat())}"
                )
                t.addTutorialTooltip("primitive_fire3")
            }
            if (event.itemStack.isOf(XenoEarlyStartItemRegistry.PLANT_FIBER.get())) {
                t.addTutorialTooltip("plant_fiber1")
                t.addTutorialTooltip(
                    "plant_fiber2",
                    "${(XenoEarlyStartConfig.config.earlyGameChanges.plantFiberDropProbability * 100).toInt()}%"
                )
            }
            if (event.itemStack.isIn(XenoEarlyStartTags.Items.PEBBLES)) {
                t.addTutorialTooltip("pebble")
                t.addItemDescriptionTooltip("pebble_to_shard")
            }
            if (event.itemStack.isIn(XenoEarlyStartTags.Items.KNAPPED_STONE)) {
                t.addTutorialTooltip("knapped_stone")
            }
            if (event.itemStack.isIn(XenoEarlyStartTags.Items.SHARDS)) {
                t.addTutorialTooltip(
                    "shards",
                    "${(XenoEarlyStartConfig.config.earlyGameChanges.plantFiberDropProbability * 100).toInt()}%"
                )
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
                t.addTutorialTooltip("flint_knapping")
            }
            if (event.itemStack.isOf(Items.WOODEN_PICKAXE)) {
                t.addTutorialTooltip("starting_guide")
            }
            if (event.itemStack.isOf(XenoEarlyStartItemRegistry.GUIDE.get())) {
                t.addItemDescriptionTooltip("guide")
                t.addItemDescriptionTooltip(
                    t.getTranslatedText("guide_more").append(
                        Text.keybind("key.advancements").setStyle(
                            AURI.withItalic(true)
                        )
                    )
                )
            }
            t.insertTooltips(event, 1)

        }
    }

    private class Tooltips {
        var tooltips = arrayListOf<MutableText>()

        private fun getTranslatedText(text: MutableText): MutableText {
            return text
        }
        fun getTranslatedText(name: String): MutableText {
            return Text.translatable(
                "xeno_early_start.item.tooltip.$name"
            )
        }

        fun getTranslatedText(name: String, vararg values: Any): MutableText {
            // For some reason you cant use %s inside § formatting code with Text.translatable, so this workaround is required
            return Text.literal(
                String.format(Language.getInstance().get("xeno_early_start.item.tooltip.$name"), *values)
            )
        }

        fun addToolTip(text: MutableText) {
            tooltips.add(
                text
            )
        }

        fun addToolTip(name: String, vararg values: Any) {
            addToolTip(
                getTranslatedText(name, *values)
            )
        }

        fun addTutorialTooltip(text: MutableText) {
            if (!XenoEarlyStartConfig.config.client.tooltips.disableTutorialTooltips) {
                addToolTip(text)
            }
        }
        fun addTutorialTooltip(name: String) {
            addTutorialTooltip(getTranslatedText(name))
        }
        fun addTutorialTooltip(name: String, vararg values: Any) {
            addTutorialTooltip(getTranslatedText(name, *values))
        }

        fun addItemDescriptionTooltip(text: MutableText) {
            if (!XenoEarlyStartConfig.config.client.tooltips.disableItemDescriptionTooltips) {
                addToolTip(text)
            }
        }

        fun addItemDescriptionTooltip(name: String, vararg values: Any) {
            addItemDescriptionTooltip(getTranslatedText(name, *values))
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
                    this.tooltips[0]
                )
                event.toolTip.add(
                    at + 1,
                    getTranslatedText("shift_hidden").setStyle(
                        Style.EMPTY.withItalic(true).withColor(0xf5bde6)
                    )
                )
            }
        }
    }
}