package com.github.auri_f5bde6.xeno_early_start.advancements

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.google.gson.JsonObject
import net.minecraft.advancement.criterion.AbstractCriterion
import net.minecraft.advancement.criterion.AbstractCriterionConditions
import net.minecraft.item.AxeItem
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer
import net.minecraft.predicate.entity.LootContextPredicate
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier

class FistBreakLogCriterion : AbstractCriterion<FistBreakLogCriterion.Conditions>() {
    companion object {
        val ID = XenoEarlyStart.of("fist_break_log")
    }

    fun trigger(player: ServerPlayerEntity) {
        this.trigger(
            player
        ) { conditions: Conditions ->
            conditions.matches(
                player,
            )
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
        return Conditions(id, playerPredicate)
    }

    class Conditions(
        id: Identifier,
        val entity: LootContextPredicate
    ) : AbstractCriterionConditions(id, entity) {
        fun matches(player: ServerPlayerEntity): Boolean {
            return player.mainHandStack.item.asItem() !is AxeItem
        }
    }
}