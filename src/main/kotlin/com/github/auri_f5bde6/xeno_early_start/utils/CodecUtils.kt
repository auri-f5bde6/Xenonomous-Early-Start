package com.github.auri_f5bde6.xeno_early_start.utils

import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.recipe.Ingredient
import net.minecraft.registry.Registries
import net.minecraft.util.dynamic.Codecs
import java.util.*
import kotlin.jvm.optionals.getOrNull

object CodecUtils {
    val INGREDIENT_CODEC: Codec<Ingredient> =
        Codecs.JSON_ELEMENT.comapFlatMap(
            tryDataResult(Ingredient::fromJson),
            Ingredient::toJson
        )

    // net.minecraft.item.ItemStack.CODEC is _weird_, count is required, oh wait, no, its `Count` not `count` ...
    val ITEM_STACK_CODEC: MapCodec<ItemStack> = RecordCodecBuilder.mapCodec { instance ->
        instance.group(
            Registries.ITEM.getCodec().fieldOf("item")
                .forGetter(ItemStack::getItem),
            Codec.INT.optionalFieldOf("count", 1).forGetter(ItemStack::getCount),
            NbtCompound.CODEC.optionalFieldOf("tag")
                .forGetter { itemStack -> Optional.ofNullable(itemStack.nbt) }
        ).apply(instance) { item, int, nbt -> ItemStack(item, int, nbt.getOrNull()) }
    }
    /*val PLAYER_PREDICATE_CODEC: Codec<PlayerPredicate> = Codecs.JSON_ELEMENT.comapFlatMap(
        tryDataResult(PlayerPredicate::fromJson),
        PlayerPredicate::toJson
    )
    val LOOT_TABLE_CODEC: Codec<LootTable> = fromSerializer(
        LootDataType.LOOT_TABLES.gson,
        LootTable::class.java
    )
    val LOOT_CONDITION_CODEC: Codec<LootCondition> = fromSerializer(
        LootDataType.PREDICATES.gson,
        LootCondition::class.java
    )

     private fun <T> fromSerializer(
         gson: Gson,
         `class`: Class<T>,
     ): Codec<T> {
         return Codecs.JSON_ELEMENT.comapFlatMap(
             tryDataResult { element -> gson.fromJson(element, `class`) },
             { obj -> gson.toJsonTree(obj) }
         )
     }*/

    /*private fun <T> tryDataResult(converter: (JsonObject) -> T): (JsonElement) -> DataResult<T> {
        return { jsonElement ->
            if (jsonElement.isJsonObject) {
                tryDataResult(converter)(jsonElement.asJsonObject)
            } else {
                DataResult.error { "Expected a json object." }
            }
        }
    }*/

    private fun <T> tryDataResult(converter: (JsonElement) -> T): (JsonElement) -> DataResult<T> {
        return { jsonElement ->
            try {
                DataResult.success(converter(jsonElement))
            } catch (e: JsonSyntaxException) {
                DataResult.error { e.message }
            } catch (e: JsonParseException) {
                DataResult.error { e.message }
            }
        }
    }

    fun <T> dataResultFromNullable(value: T?, error: () -> String): DataResult<T> {
        return if (value == null) {
            DataResult.error(error)
        } else {
            DataResult.success(value)
        }
    }
}