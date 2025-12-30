package net.hellomouse.xeno_early_start.loot

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import net.hellomouse.xeno_early_start.registries.ProgressionModLootTypeRegistry
import net.minecraft.loot.condition.LootCondition
import net.minecraft.loot.condition.LootConditionType
import net.minecraft.loot.context.LootContext
import net.minecraft.loot.context.LootContextParameters
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import net.minecraft.util.JsonSerializer
import net.minecraftforge.registries.ForgeRegistries


class BlockIsInTagCondition private constructor(private val tag: Identifier) : LootCondition {


    override fun getType(): LootConditionType {
        return ProgressionModLootTypeRegistry.blockIsInTagCondition.get()
    }

    override fun test(lootContext: LootContext): Boolean {
        return lootContext.get(LootContextParameters.BLOCK_STATE)
            ?.isIn(TagKey.of(ForgeRegistries.BLOCKS.getRegistryKey(), tag)) ?: false
    }

    class Serializer : JsonSerializer<BlockIsInTagCondition> {
        override fun toJson(json: JsonObject, `object`: BlockIsInTagCondition, context: JsonSerializationContext) {
            json.addProperty("tag", `object`.tag.toString())
        }

        override fun fromJson(json: JsonObject, context: JsonDeserializationContext): BlockIsInTagCondition {
            return BlockIsInTagCondition(
                Identifier.parse(json.get("tag").asString)
            )
        }
    }
}