package com.github.auri_f5bde6.xeno_early_start.utils

import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import com.mojang.serialization.Codec
import com.mojang.serialization.JsonOps
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.util.Identifier

interface RequireId<T : Recipe<*>> {
    fun withId(id: Identifier): T
}

abstract class CodecRecipeSerializer<T : Recipe<*>, R : RequireId<T>>(val codec: Codec<R>) : RecipeSerializer<T> {
    override fun read(id: Identifier, json: JsonObject): T {
        return codec.parse(JsonOps.INSTANCE, json)
            .getOrThrow(false) { msg -> throw JsonSyntaxException(msg) }.withId(id)
    }
}