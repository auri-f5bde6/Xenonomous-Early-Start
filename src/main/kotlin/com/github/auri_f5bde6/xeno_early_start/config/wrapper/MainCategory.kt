package com.github.auri_f5bde6.xeno_early_start.config.wrapper

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry
import me.shedaniel.clothconfig2.api.ConfigBuilder
import me.shedaniel.clothconfig2.api.ConfigCategory

class MainCategory(
    override val wrapper: ConfigWrapper,
    configBuilder: ConfigBuilder,
    override val categoryName: String,
    override val translationKeyPrefix: String,
    override val overrideCategoryName: String? = null
) : Category() {
    val category: ConfigCategory = configBuilder.getOrCreateCategory(getCategoryTranslatedText())
    override fun <T> addEntry(entry: AbstractConfigListEntry<T>) {
        category.addEntry(entry)
    }
}