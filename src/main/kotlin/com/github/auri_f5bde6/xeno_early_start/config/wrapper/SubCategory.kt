package com.github.auri_f5bde6.xeno_early_start.config.wrapper

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder

class SubCategory(
    override val wrapper: ConfigWrapper,
    override val categoryName: String,
    expanded: Boolean = false,
    override val translationKeyPrefix: String,
) : Category() {
    val builder: SubCategoryBuilder =
        entryBuilder.startSubCategory(getCategoryTranslatedText()).setExpanded(expanded)

    override fun <T> addEntry(entry: AbstractConfigListEntry<T>) {
        builder.add(entry)
    }
}