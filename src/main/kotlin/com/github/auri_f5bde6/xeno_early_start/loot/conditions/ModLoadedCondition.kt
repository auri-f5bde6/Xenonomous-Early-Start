package com.github.auri_f5bde6.xeno_early_start.loot.conditions

import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartLootTypeRegistry
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSyntaxException
import com.mojang.serialization.Codec
import com.mojang.serialization.JsonOps
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.loot.condition.LootCondition
import net.minecraft.loot.condition.LootConditionType
import net.minecraft.loot.context.LootContext
import net.minecraft.util.JsonSerializer
import net.minecraftforge.fml.ModList

class ModLoadedCondition(val modid: String) : LootCondition {
    companion object {
        val CODEC: MapCodec<ModLoadedCondition> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.STRING.fieldOf("modid").forGetter(
                    ModLoadedCondition::modid
                )
            ).apply(instance, ::ModLoadedCondition)
        }
    }

    override fun getType(): LootConditionType {
        return XenoEarlyStartLootTypeRegistry.modLoadedCondition.get()
    }

    override fun test(t: LootContext): Boolean {
        return ModList.get().isLoaded(modid)
    }

    class Serializer : JsonSerializer<ModLoadedCondition> {
        override fun toJson(
            json: JsonObject,
            `object`: ModLoadedCondition,
            context: JsonSerializationContext
        ) {
            CODEC.codec().encode(`object`, JsonOps.INSTANCE, json)
        }

        override fun fromJson(
            json: JsonObject,
            context: JsonDeserializationContext
        ): ModLoadedCondition {
            return CODEC.codec().parse(JsonOps.INSTANCE, json)
                .getOrThrow(false) { msg -> throw JsonSyntaxException(msg) }
        }

    }
}