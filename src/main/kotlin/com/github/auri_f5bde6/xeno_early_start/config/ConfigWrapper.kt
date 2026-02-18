package com.github.auri_f5bde6.xeno_early_start.config

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry
import me.shedaniel.clothconfig2.api.ConfigBuilder
import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import me.shedaniel.clothconfig2.impl.builders.AbstractFieldBuilder
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder
import net.minecraft.text.Text
import kotlin.reflect.KMutableProperty0

class ConfigWrapper {
    val configBuilder: ConfigBuilder
    val entryBuilder: ConfigEntryBuilder
    val modID: String

    constructor(
        modID: String,
    ) {
        configBuilder = ConfigBuilder.create()
            .setTitle(getConfigTitleTranslationText(modID))
            .transparentBackground()
        entryBuilder = ConfigEntryBuilder.create()
        this.modID = modID
    }

    interface TooltipText {
        fun getText(categoryTranslationKey: String, fieldTranslationKey: String): Text
    }

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

    private fun getConfigOptionTranslationKey(): String {
        return "${getConfigTranslationKey(modID)}.option"
    }

    fun newCategory(categoryName: String): Category.MainCategory {
        return Category.MainCategory(configBuilder, entryBuilder, categoryName, getConfigOptionTranslationKey())
    }

    abstract class Category {
        fun newSubCategory(subCategoryName: String, expanded: Boolean = false): SubCategory {
            return SubCategory(
                entryBuilder,
                subCategoryName,
                expanded,
                getCategoryTranslationKey()
            )
        }

        fun addSubCategory(subCategory: SubCategory) {
            addEntry(subCategory.builder.build())
        }
        class MainCategory(
            configBuilder: ConfigBuilder,
            override val entryBuilder: ConfigEntryBuilder,
            override val categoryName: String,
            configTranslationPrefix: String
        ) : Category() {
            override val translationKeyPrefix: String = configTranslationPrefix
            val category: ConfigCategory = configBuilder.getOrCreateCategory(getCategoryTranslatedText())

            override fun <T> addEntry(entry: AbstractConfigListEntry<T>) {
                category.addEntry(entry)
            }
        }

        class SubCategory(
            override val entryBuilder: ConfigEntryBuilder,
            override val categoryName: String,
            expanded: Boolean = false,
            override val translationKeyPrefix: String
        ) : Category() {
            val builder: SubCategoryBuilder =
                entryBuilder.startSubCategory(getCategoryTranslatedText()).setExpanded(expanded)

            override fun <T> addEntry(entry: AbstractConfigListEntry<T>) {
                builder.add(entry)
            }
        }

        abstract val entryBuilder: ConfigEntryBuilder
        abstract val categoryName: String
        abstract fun <T> addEntry(entry: AbstractConfigListEntry<T>)
        protected abstract val translationKeyPrefix: String


        protected fun getCategoryTranslationKey(): String {
            return "$translationKeyPrefix.$categoryName"
        }

        protected fun getCategoryTranslatedText(): Text {
            return Text.translatable(getCategoryTranslationKey())
        }


        protected fun getFieldTranslationKey(name: String): String {
            return "${getCategoryTranslationKey()}.$name"
        }

        protected fun getFieldTranslatedText(name: String): Text {
            return Text.translatable(getFieldTranslationKey(name))
        }

        protected fun <T : AbstractFieldBuilder<*, *, *>> doStuff(
            name: String,
            tooltip: TooltipText?,
            requireRestart: Boolean,
            entry: T
        ): T {
            var e = entry
            if (tooltip != null) {
                e = e.setTooltip(tooltip.getText(getCategoryTranslationKey(), getFieldTranslationKey(name))) as T
            }
            if (requireRestart) {
                e = e.requireRestart() as T
            }
            return e
        }

        /*
        @param defaultValue the default value of the setting in floating point (0 for 0%, 1 for 100%)
        */
        fun addPercentageSlider(
            name: String,
            ref: KMutableProperty0<Float>,
            defaultValue: Float,
            min: Float,
            max: Float,
            tooltip: TooltipText? = null
        ) {
            addIntSlider(
                name,
                ref,
                defaultValue,
                min,
                max,
                { f: Float -> (f * 100.0).toInt() },
                { i: Int -> i / 100.0f },
                tooltip
            )
        }

        /*
        Add a slider from 0%-100% and save it as a float between 0-1
        @param defaultValue the default value of the setting in floating point (0-1)
        */
        fun addPercentageSlider(
            name: String,
            ref: KMutableProperty0<Float>,
            defaultValue: Float,
            tooltip: TooltipText? = null
        ) {
            addPercentageSlider(
                name,
                ref,
                defaultValue,
                0.0f,
                1.0f,
                tooltip
            )
        }

        fun addFloatField(
            name: String,
            ref: KMutableProperty0<Float>,
            defaultValue: Float,
            minMax: Pair<Float, Float>? = null,
            tooltip: TooltipText? = null,
            requireRestart: Boolean = false
        ) {
            var builder =
                doStuff(
                    name, tooltip, requireRestart,
                    entryBuilder.startFloatField(
                        getFieldTranslatedText(name),
                        ref.get()
                    )
                ).setSaveConsumer { aFloat: Float ->
                    ref.set(aFloat)
                }
                    .setDefaultValue(defaultValue)
            if (minMax != null) {
                builder = builder.setMin(minMax.first).setMax(minMax.second)
            }
            addEntry(
                builder.build()
            )
        }

        fun <T : Enum<*>> addEnumSelector(
            name: String,
            ref: KMutableProperty0<T>,
            enumClass: Class<T>,
            defaultValue: T,
            tooltip: TooltipText? = null,
            requireRestart: Boolean = false
        ) {
            addEntry(
                doStuff(
                    name, tooltip, requireRestart,
                    entryBuilder.startEnumSelector(
                        getFieldTranslatedText(name),
                        enumClass,
                        ref.get()
                    )
                ).setSaveConsumer { enum: T ->
                    ref.set(enum)
                }
                    .setDefaultValue(defaultValue)
                    .build()
            )
        }

        fun addBooleanToggle(
            name: String,
            ref: KMutableProperty0<Boolean>,
            defaultValue: Boolean,
            tooltip: TooltipText? = null,
            requireRestart: Boolean = false
        ) {
            addEntry(
                doStuff(
                    name, tooltip, requireRestart,
                    entryBuilder.startBooleanToggle(
                        getFieldTranslatedText(name),
                        ref.get(),
                    )
                ).setSaveConsumer { aBoolean: Boolean ->
                    ref.set(aBoolean)
                }
                    .setDefaultValue(defaultValue)
                    .build()
            )
        }

        fun <T> addIntSlider(
            name: String,
            ref: KMutableProperty0<T>,
            defaultValue: T,
            from: T,
            to: T,
            toGui: (T) -> Int,
            fromGui: (Int) -> T,
            tooltip: TooltipText? = null,
            requireRestart: Boolean = false
        ) {
            addEntry(
                doStuff(
                    name, tooltip, requireRestart,
                    entryBuilder.startIntSlider(
                        getFieldTranslatedText(name),
                        toGui(ref.get()),
                        toGui(from),
                        toGui(to),
                    )
                ).setSaveConsumer { aInt: Int ->
                    ref.set(fromGui(aInt))
                }
                    .setDefaultValue(toGui(defaultValue)).build()
            )
        }

        fun addIntSlider(
            name: String,
            ref: KMutableProperty0<Int>,
            defaultValue: Int,
            from: Int,
            to: Int,
            tooltip: TooltipText? = null,
            requireRestart: Boolean = false
        ) {
            addIntSlider(name, ref, defaultValue, from, to, { v: Int -> v }, { v: Int -> v }, tooltip, requireRestart)
        }
    }
}