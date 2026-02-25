package com.github.auri_f5bde6.xeno_early_start.advancements

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.google.gson.JsonObject
import net.minecraft.advancement.criterion.AbstractCriterion
import net.minecraft.advancement.criterion.AbstractCriterionConditions
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer
import net.minecraft.predicate.entity.LootContextPredicate
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier

class CoalDustCriterion : AbstractCriterion<CoalDustCriterion.Conditions>() {
    companion object {
        val ID = XenoEarlyStart.of("coal_dust")
    }

    fun trigger(player: ServerPlayerEntity, type: Conditions.Type) {
        this.trigger(
            player
        ) { conditions: Conditions ->
            conditions.matches(type)
        }
    }

    override fun getId(): Identifier {
        return ID
    }

    override fun conditionsFromJson(
        obj: JsonObject,
        playerPredicate: LootContextPredicate,
        predicateDeserializer: AdvancementEntityPredicateDeserializer?
    ): Conditions {
        return Conditions(
            id,
            playerPredicate,
            Conditions.Type.fromString(obj.get("type").asString)
                ?: throw IllegalArgumentException("Unknown value ${obj.get("type").asString} in type field of CoalDustCriterion"),
        )
    }

    class Conditions(
        id: Identifier,
        val entity: LootContextPredicate,
        val type: Type
    ) : AbstractCriterionConditions(id, entity) {
        enum class Type(val literal: String) {
            INFLICTION("infliction"),
            EXPLOSION("explosion");

            companion object {
                fun fromString(type: String) = Type.entries.find { v -> v.literal == type }
            }
        }

        fun matches(type: Type): Boolean {
            return type == this.type
        }
    }
}