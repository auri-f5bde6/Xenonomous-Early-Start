package net.hellomouse.xeno_early_start.utils

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import net.minecraft.util.JsonHelper
import net.minecraftforge.registries.ForgeRegistries

/// All function here takes an JsonObject, locate the key, return null if the key is not present. But if the value is invalid, it throws error
object JsonUtils {
    fun getItem(obj: JsonObject, key: String): Item? {
        val value = getIdentifier(obj, key)
        if (value == null) {
            return null
        }
        val item = ForgeRegistries.ITEMS.getValue(value)
        if (item == null) {
            throw JsonSyntaxException(
                "Expected $key to be a valid Minecraft item identifier, got $value "
            )
        }
        return item
    }

    fun getIdentifier(obj: JsonObject, key: String): Identifier? {
        val value = getString(obj, key)
        if (value == null) {
            return null
        }
        val ident = Identifier.tryParse(value)
        if (ident == null) {
            throw JsonSyntaxException(
                "Expected $key to be a valid Minecraft resource identifier, got $value"
            )
        }
        return ident
    }

    fun getString(obj: JsonObject, key: String): String? {
        val value = obj.get(key)
        if (value == null) {
            return null
        }
        if (value.isJsonPrimitive && value.asJsonPrimitive.isString) {
            return value.asString
        }
        throw JsonSyntaxException(
            "Expected $key to be a JsonPrimitive String, was " + JsonHelper.getType(
                value
            )
        )

    }

    fun getBool(obj: JsonObject, key: String): Boolean? {
        val value = obj.get(key)
        if (value == null) {
            return null
        }
        if (value.isJsonPrimitive && value.asJsonPrimitive.isBoolean) {
            return value.asBoolean
        }
        throw JsonSyntaxException(
            "Expected $key to be a JsonPrimitive Boolean, was " + JsonHelper.getType(
                value
            )
        )

    }

    fun getFloat(obj: JsonObject, key: String): Float? {
        val value = obj.get(key)
        if (value == null) {
            return null
        }
        if (value.isJsonPrimitive && value.asJsonPrimitive.isNumber) {
            return value.asFloat
        }
        throw JsonSyntaxException(
            "Expected $key to be a JsonPrimitive Number, was " + JsonHelper.getType(
                value
            )
        )

    }

    inline fun <reified T> getArray(obj: JsonObject, key: String, mapper: (JsonElement) -> T): Array<T>? {
        val value = obj.get(key)
        if (value == null) {
            return null
        }
        if (value.isJsonArray) {
            val array = arrayOfNulls<T>(value.asJsonArray.size())
            for ((index, item) in value.asJsonArray.withIndex()) {
                array[index] = mapper(item)
            }
            @Suppress("UNCHECKED_CAST") // It's fine, cause return value of mapper is never null
            return array as Array<T>
        }
        throw JsonSyntaxException(
            "Expected $key to be a JsonArray, was " + JsonHelper.getType(
                value
            )
        )

    }
}