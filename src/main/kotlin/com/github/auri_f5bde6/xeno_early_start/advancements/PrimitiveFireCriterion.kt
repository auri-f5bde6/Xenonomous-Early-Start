package com.github.auri_f5bde6.xeno_early_start.advancements

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.github.auri_f5bde6.xeno_early_start.advancements.PrimitiveFireCriterion.Conditions.Type
import com.google.gson.JsonObject
import net.minecraft.advancement.criterion.AbstractCriterion
import net.minecraft.advancement.criterion.AbstractCriterionConditions
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer
import net.minecraft.predicate.entity.LootContextPredicate
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import net.minecraftforge.registries.ForgeRegistries

class PrimitiveFireCriterion : AbstractCriterion<PrimitiveFireCriterion.Conditions>() {
    companion object {
        val ID = XenoEarlyStart.of("primitive_fire")
    }

    override fun conditionsFromJson(
        obj: JsonObject,
        playerPredicate: LootContextPredicate,
        predicateDeserializer: AdvancementEntityPredicateDeserializer
    ): Conditions {
        var item: Item? = null
        val itemId = obj.get("item")
        if (itemId != null && !itemId.isJsonNull) {
            item = ForgeRegistries.ITEMS.getValue(Identifier.parse(itemId.asString))
        }
        return Conditions(
            id,
            playerPredicate,
            Type.fromString(obj.get("type").asString)
                ?: throw IllegalArgumentException("Unknown value ${obj.get("type").asString} in type field of PrimitiveFireCriterion"),
            item
        )
    }

    fun trigger(player: ServerPlayerEntity, type: Type, itemStack: ItemStack? = null) {
        this.trigger(
            player
        ) { conditions -> conditions.matches(type, itemStack) }
    }

    override fun getId(): Identifier {
        return ID
    }

    class Conditions(id: Identifier, entity: LootContextPredicate, val type: Type, val item: Item? = null) :
        AbstractCriterionConditions(id, entity) {
        enum class Type(val literal: String) {
            CREATE("create"),
            REFUEL("refuel"),
            USE("use");

            companion object {
                fun fromString(type: String) = Type.entries.find { v -> v.literal == type }
            }
        }

        fun matches(type: Type, itemStack: ItemStack? = null): Boolean {
            if (this.type != type) {
                return false
            }
            if (this.type == Type.USE) {
                if (itemStack == null) return false
                return itemStack.isOf(item)
            }
            return true
        }

        override fun toJson(predicateSerializer: AdvancementEntityPredicateSerializer?): JsonObject? {
            val jsonObject = super.toJson(predicateSerializer)
            return jsonObject
        }
    }
}