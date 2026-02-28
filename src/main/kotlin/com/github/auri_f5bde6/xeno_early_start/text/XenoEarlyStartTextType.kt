package com.github.auri_f5bde6.xeno_early_start.text

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStartConfig
import com.github.auri_f5bde6.xeno_early_start.event.ItemTooltipEventListener.calculateNumberOfStickRequired
import net.minecraft.text.MutableText

object XenoEarlyStartTextType {

    @JvmStatic
    fun getText(value: String): MutableText {
        return XenoEarlyStartTextContent.newText(value) {
            when (value) {
                "advancements.xeno_early_start.plant_fiber.description" -> arrayOf(
                    "${(XenoEarlyStartConfig.config.earlyGameChanges.plantFiberDropProbability * 100).toInt()}%"
                )

                "advancements.xeno_early_start.primitive_fire.description" -> arrayOf(
                    "${(XenoEarlyStartConfig.config.earlyGameChanges.primitiveFire.percentageRequiredForMaxBrightness * XenoEarlyStartConfig.config.earlyGameChanges.primitiveFire.maxBurnTime / 20 / 60).toInt()}",
                    "${
                        calculateNumberOfStickRequired(
                            XenoEarlyStartConfig.config.earlyGameChanges.primitiveFire.percentageRequiredForMaxBrightness * XenoEarlyStartConfig.config.earlyGameChanges.primitiveFire.maxBurnTime
                        )
                    }"
                )

                else -> throw IllegalArgumentException("Invalid xeno early start text type '$value'")
            }

        }

    }
}