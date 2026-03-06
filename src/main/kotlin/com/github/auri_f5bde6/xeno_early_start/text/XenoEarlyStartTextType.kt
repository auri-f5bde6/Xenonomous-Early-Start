package com.github.auri_f5bde6.xeno_early_start.text

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStartConfig
import com.github.auri_f5bde6.xeno_early_start.event.ItemTooltipEventListener.calculateNumberOfStickRequired
import net.minecraft.text.MutableText

object XenoEarlyStartTextType {

    val customTexts: Map<String, () -> Array<Any>> = mapOf(
        "advancements.xeno_early_start.plant_fiber.description" to {
            arrayOf(
                "${(XenoEarlyStartConfig.config.earlyGameChanges.plantFiberDropProbability * 100).toInt()}%"
            )
        },
        "advancements.xeno_early_start.fire_starter.description" to {
            arrayOf(
                "${(XenoEarlyStartConfig.config.earlyGameChanges.primitiveFire.percentageRequiredForMaxBrightness * XenoEarlyStartConfig.config.earlyGameChanges.primitiveFire.maxBurnTime / 20 / 60).toInt()}",
                "${
                    calculateNumberOfStickRequired(
                        XenoEarlyStartConfig.config.earlyGameChanges.primitiveFire.percentageRequiredForMaxBrightness * XenoEarlyStartConfig.config.earlyGameChanges.primitiveFire.maxBurnTime
                    )
                }"
            )
        }
    )

    @JvmStatic
    fun getText(value: String): MutableText? {
        val func = customTexts[value] ?: return null
        return MutableText.of(XenoEarlyStartTextContent(value, func))
    }

}