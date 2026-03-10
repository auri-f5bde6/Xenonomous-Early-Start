package com.github.auri_f5bde6.xeno_early_start.loot

import com.github.auri_f5bde6.xeno_early_start.config.XenoEarlyStartConfig
import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartLootTypeRegistry
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import net.minecraft.loot.condition.LootCondition
import net.minecraft.loot.condition.LootConditionType
import net.minecraft.loot.context.LootContext
import net.minecraft.util.JsonSerializer
import kotlin.reflect.KProperty0


class ConfigLootCondition private constructor(private val config: Type) : LootCondition {

    enum class Type(val literal: String, val config: KProperty0<Boolean>) {
        VANILLA_COPPER_LOOT_TABLE(
            "vanilla_copper_loot_table",
            XenoEarlyStartConfig.config.oreChanges::vanillaCopperLootTable
        ),
        VANILLA_IRON_LOOT_TABLE(
            "vanilla_iron_loot_table",
            XenoEarlyStartConfig.config.oreChanges::vanillaIronLootTable
        ),
        VANILLA_GOLD_LOOT_TABLE(
            "vanilla_gold_loot_table",
            XenoEarlyStartConfig.config.oreChanges::vanillaGoldLootTable
        ),
        VANILLA_DIAMOND_LOOT_TABLE(
            "vanilla_diamond_loot_table",
            XenoEarlyStartConfig.config.oreChanges::vanillaDiamondLootTable
        );

        fun test(): Boolean {
            return this.config.get()
        }

        companion object {
            fun fromString(type: String) = Type.entries.find { v -> v.literal == type }
        }
    }

    override fun getType(): LootConditionType {
        return XenoEarlyStartLootTypeRegistry.configLootCondition.get()
    }

    override fun test(lootContext: LootContext): Boolean {
        return config.test()
    }

    class Serializer : JsonSerializer<ConfigLootCondition> {
        override fun toJson(json: JsonObject, `object`: ConfigLootCondition, context: JsonSerializationContext) {
            json.addProperty("config", `object`.config.literal)
        }

        override fun fromJson(json: JsonObject, context: JsonDeserializationContext): ConfigLootCondition {
            return ConfigLootCondition(
                Type.fromString(json.get("config").asString)
                    ?: throw IllegalArgumentException("No config called ${json.get("config").asString} found")
            )
        }
    }
}