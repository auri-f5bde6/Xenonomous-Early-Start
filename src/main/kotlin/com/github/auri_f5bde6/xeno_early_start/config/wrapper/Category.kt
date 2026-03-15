package com.github.auri_f5bde6.xeno_early_start.config.wrapper

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import me.shedaniel.clothconfig2.impl.builders.AbstractFieldBuilder
import net.minecraft.text.Text
import kotlin.reflect.KMutableProperty0

abstract class Category {

    abstract val wrapper: ConfigWrapper
    abstract val translationKeyPrefix: String
    abstract val categoryName: String
    abstract fun <T> addEntry(entry: AbstractConfigListEntry<T>)


    open val overrideCategoryName: String? = null
    val entryBuilder: ConfigEntryBuilder
        get() = wrapper.entryBuilder

    fun <T> addSubCategory(subCategoryName: String, expanded: Boolean, data: T, body: (SubCategory, T) -> Unit) {
        val category = SubCategory(
            wrapper,
            subCategoryName,
            expanded,
            getCategoryTranslationKey()
        )
        body(category, data)
        addEntry(category.builder.build())
    }


    fun <T> addSubCategory(subCategoryName: String, data: T, body: (SubCategory, T) -> Unit) {
        addSubCategory(subCategoryName, false, data, body)
    }

    fun addSubCategory(subCategoryName: String, expanded: Boolean, body: (SubCategory) -> Unit) {
        addSubCategory(subCategoryName, expanded, Unit) { subCategory, _ -> body(subCategory) }
    }

    fun addSubCategory(subCategoryName: String, body: (SubCategory) -> Unit) {
        addSubCategory(subCategoryName, false, body)
    }


    protected fun getCategoryTranslationKey(): String {
        return "$translationKeyPrefix.$categoryName"
    }

    protected fun getCategoryTranslatedText(): Text {
        if (overrideCategoryName != null) {
            return Text.translatable( "$translationKeyPrefix.$overrideCategoryName")
        } else {
            return Text.translatable(getCategoryTranslationKey())
        }
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
        addSlider(
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

    fun <T> addSlider(
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
        addSlider(name, ref, defaultValue, from, to, { v: Int -> v }, { v: Int -> v }, tooltip, requireRestart)
    }
}