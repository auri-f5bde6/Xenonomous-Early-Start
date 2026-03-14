package com.github.auri_f5bde6.xeno_early_start.config.wrapper

import net.minecraft.text.Text

interface TooltipText {
    fun getText(categoryTranslationKey: String, fieldTranslationKey: String): Text
    class DefaultTooltip : TooltipText {
        override fun getText(categoryTranslationKey: String, fieldTranslationKey: String): Text {
            return Text.translatable("$fieldTranslationKey.tooltip")
        }
    }

    class CustomTooltip(val customTooltipKey: String) : TooltipText {
        override fun getText(categoryTranslationKey: String, fieldTranslationKey: String): Text {
            return Text.translatable("$categoryTranslationKey.$customTooltipKey.tooltip")
        }
    }
}

