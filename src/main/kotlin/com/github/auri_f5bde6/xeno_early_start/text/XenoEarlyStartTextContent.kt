package com.github.auri_f5bde6.xeno_early_start.text

import net.minecraft.text.*
import net.minecraft.util.Language
import java.util.*

class XenoEarlyStartTextContent(val key: String, val value: () -> Array<out Any>) : TextContent {
    companion object {
        fun newText(id: String, value: () -> Array<out Any>): MutableText {
            return MutableText.of(XenoEarlyStartTextContent(id, value))
        }

        private fun getTranslatedText(key: String, vararg values: Any): MutableText {
            // For some reason you cant use %s inside § formatting code with Text.translatable, so this workaround is required
            return Text.literal(
                String.format(Language.getInstance().get(key), *values)
            )
        }
    }

    override fun <T> visit(visitor: StringVisitable.StyledVisitor<T>, style: Style): Optional<T> {
        return getTranslatedText(key, *(value.invoke())).visit(visitor, style)
    }

    override fun hashCode(): Int {
        return key.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return other is XenoEarlyStartTextContent && other.key == key
    }

    override fun toString(): String {
        return "xeno_early_start{" + this.key + "}"
    }
}