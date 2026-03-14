package com.github.auri_f5bde6.xeno_early_start.config.wrapper

import me.shedaniel.clothconfig2.api.ConfigBuilder
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import net.minecraft.text.Text

class ConfigWrapper {
    val configBuilder: ConfigBuilder
    val entryBuilder: ConfigEntryBuilder
    val modID: String

    companion object {
        private fun getConfigTranslationKey(modID: String): String {
            return "text.config.${modID}"
        }

        fun getConfigTitleTranslationKey(modID: String): String {
            return "${getConfigTranslationKey(modID)}.title"
        }

        fun getConfigTitleTranslationText(modID: String): Text {
            return Text.translatable(getConfigTitleTranslationKey(modID))
        }
    }

    constructor(
        modID: String
    ) {
        configBuilder = ConfigBuilder.create()
            .setTitle(getConfigTitleTranslationText(modID))
            .transparentBackground()
        entryBuilder = ConfigEntryBuilder.create()
        this.modID = modID
    }


    private fun getConfigOptionTranslationKey(): String {
        return "${getConfigTranslationKey(modID)}.option"
    }

    private fun newCategory(categoryName: String, overrideCategoryName: String?): MainCategory {
        return MainCategory(this, configBuilder, categoryName, getConfigOptionTranslationKey(), overrideCategoryName)
    }

    fun <T> newCategory(
        categoryName: String,
        overrideCategoryName: String?,
        data: T,
        body: (MainCategory, T) -> Unit
    ): MainCategory {
        val category = newCategory(categoryName, overrideCategoryName)
        body(category, data)
        return category
    }

    fun newCategory(categoryName: String, overrideCategoryName: String?, body: (MainCategory) -> Unit): MainCategory {
        return newCategory(categoryName, overrideCategoryName, Unit) { mainCategory, _ -> body(mainCategory) }
    }

    fun <T> newCategory(categoryName: String, data: T, body: (MainCategory, T) -> Unit): MainCategory {
        return newCategory(categoryName, null, data, body)
    }

    fun newCategory(categoryName: String, body: (MainCategory) -> Unit): MainCategory {
        return newCategory(categoryName, null, body)
    }

}