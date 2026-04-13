package com.github.auri_f5bde6.xeno_early_start.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSyntaxException
import com.mojang.serialization.Codec
import com.mojang.serialization.JsonOps
import net.minecraft.util.JsonSerializer

class CodecJsonSerializer<T>(val codec: Codec<T>) : JsonSerializer<T> {
    override fun toJson(
        json: JsonObject,
        `object`: T,
        context: JsonSerializationContext
    ) {
        codec.encode(`object`, JsonOps.INSTANCE, json)
    }

    override fun fromJson(json: JsonObject, context: JsonDeserializationContext): T {
        return codec.parse(JsonOps.INSTANCE, json)
            .getOrThrow(false) { msg -> throw JsonSyntaxException(msg) }
    }
}