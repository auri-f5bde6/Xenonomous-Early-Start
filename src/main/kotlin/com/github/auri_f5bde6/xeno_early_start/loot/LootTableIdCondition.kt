package com.github.auri_f5bde6.xeno_early_start.loot

import com.github.auri_f5bde6.xeno_early_start.registries.ProgressionModLootTypeRegistry
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import net.minecraft.loot.condition.LootCondition
import net.minecraft.loot.condition.LootConditionType
import net.minecraft.loot.context.LootContext
import net.minecraft.util.Identifier
import net.minecraft.util.JsonSerializer

class LootTableIdCondition private constructor(private val targetLootTableId: Identifier) : LootCondition {


    override fun getType(): LootConditionType {
        return ProgressionModLootTypeRegistry.lootTableIdCondition.get()
    }

    override fun test(lootContext: LootContext): Boolean {
        return lootContext.queriedLootTableId.equals(this.targetLootTableId)
    }


    class Serializer : JsonSerializer<LootTableIdCondition> {
        override fun toJson(json: JsonObject, `object`: LootTableIdCondition, context: JsonSerializationContext) {
            json.addProperty("loot_table_id", `object`.targetLootTableId.toString())
        }

        override fun fromJson(json: JsonObject, context: JsonDeserializationContext): LootTableIdCondition {
            return LootTableIdCondition(
                Identifier.parse(json.get("loot_table_id").asString)
            )
        }
    }
}