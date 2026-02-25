package com.github.auri_f5bde6.xeno_early_start.advancements

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.google.gson.JsonObject
import com.mojang.datafixers.util.Either
import net.minecraft.advancement.criterion.AbstractCriterion
import net.minecraft.advancement.criterion.AbstractCriterionConditions
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer
import net.minecraft.predicate.entity.LootContextPredicate
import net.minecraft.registry.tag.TagKey
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import net.minecraftforge.registries.ForgeRegistries

class BreakBlockCriterion : AbstractCriterion<BreakBlockCriterion.Conditions>() {
    companion object {
        val ID = XenoEarlyStart.of("break_block")
    }

    override fun conditionsFromJson(
        obj: JsonObject,
        playerPredicate: LootContextPredicate,
        predicateDeserializer: AdvancementEntityPredicateDeserializer
    ): Conditions {
        val block = obj.get("block").asString
        return if (block.startsWith("#")) {
            Conditions(
                id,
                playerPredicate,
                TagKey.of(ForgeRegistries.BLOCKS.getRegistryKey(), Identifier.parse(block.substring(1)))
            )
        } else {
            Conditions(
                id, playerPredicate, ForgeRegistries.BLOCKS.getValue(Identifier.parse(block))
                    ?: throw IllegalArgumentException("Unknown block: $block")
            )
        }
    }

    fun trigger(player: ServerPlayerEntity, blockState: BlockState) {
        this.trigger(
            player
        ) { conditions: Conditions ->
            conditions.matches(
                blockState
            )
        }
    }

    override fun getId(): Identifier {
        return ID
    }

    class Conditions : AbstractCriterionConditions {
        val blockOrTag: Either<Block, TagKey<Block>>

        constructor(id: Identifier, entity: LootContextPredicate, block: Block) : super(id, entity) {
            blockOrTag = Either.left(block)
        }

        constructor(id: Identifier, entity: LootContextPredicate, tag: TagKey<Block>) : super(id, entity) {
            blockOrTag = Either.right(tag)
        }

        fun matches(blockState: BlockState): Boolean {
            return blockOrTag.map({ block -> blockState.isOf(block) }, { tag -> blockState.isIn(tag) })
        }

        override fun toJson(predicateSerializer: AdvancementEntityPredicateSerializer?): JsonObject? {
            val jsonObject = super.toJson(predicateSerializer)
            jsonObject.addProperty(
                "block", blockOrTag.map(
                    { block ->
                        ForgeRegistries.BLOCKS.getKey(block).toString()
                    },
                    { tag -> "#${tag.id}" }
                ))
            return jsonObject
        }
    }
}