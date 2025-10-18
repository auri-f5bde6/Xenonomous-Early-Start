package net.hellomouse.xeno_early_start.loot

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import net.hellomouse.xeno_early_start.ProgressionMod
import net.hellomouse.xeno_early_start.registries.ProgressionModLootTypeRegistry
import net.hellomouse.xeno_early_start.utils.MiningLevel
import net.minecraft.item.ToolMaterial
import net.minecraft.loot.condition.LootCondition
import net.minecraft.loot.condition.LootConditionType
import net.minecraft.loot.context.LootContext
import net.minecraft.loot.context.LootContextParameters
import net.minecraft.util.Identifier
import net.minecraft.util.InvalidIdentifierException
import net.minecraft.util.JsonSerializer
import net.minecraftforge.common.TierSortingRegistry

class MatchToolTier(var tier: ToolMaterial?, var cond: Condition) : LootCondition {
    enum class Condition {
        Greater,
        GreaterEqual,
        Lesser,
        LessEqual,
        Equal;

        companion object {
            fun fromJson(string: String): Condition? {
                when (string.lowercase()) {
                    "greater", ">" -> return Greater
                    "greaterEqual", ">=" -> return GreaterEqual
                    "lesser", "<" -> return Lesser
                    "equal", "==" -> return Equal
                }
                return null
            }
        }

        fun toJson(): String {
            return when (this) {
                Greater -> ">"
                GreaterEqual -> ">="
                Lesser -> "<"
                LessEqual -> "<="
                Equal -> "=="
            }
        }
    }

    override fun getType(): LootConditionType {
        return ProgressionModLootTypeRegistry.matchToolTier.get()
    }

    override fun test(lootContext: LootContext): Boolean {
        val itemStack = lootContext.get(LootContextParameters.TOOL)
        return when (cond) {
            Condition.Greater -> {
                MiningLevel.isToolGreaterThanTier(itemStack, tier)
            }

            Condition.GreaterEqual -> {
                MiningLevel.isToolGreaterOrEqualThanTier(itemStack, tier)
            }

            Condition.Lesser -> {
                MiningLevel.isToolLowerThanTier(itemStack, tier)
            }

            Condition.LessEqual -> {
                MiningLevel.isToolLowerOrEqualThanTier(itemStack, tier)
            }

            Condition.Equal -> {
                MiningLevel.isToolEqualToTier(itemStack, tier)
            }
        }
    }

    class Serializer : JsonSerializer<MatchToolTier> {
        override fun toJson(
            jsonObject: JsonObject,
            matchToolTier: MatchToolTier,
            jsonSerializationContext: JsonSerializationContext?
        ) {
            jsonObject.addProperty("tier", MiningLevel.getTierName(matchToolTier.tier))
            jsonObject.addProperty("cond", matchToolTier.cond.toJson())
        }

        override fun fromJson(
            jsonObject: JsonObject,
            jsonDeserializationContext: JsonDeserializationContext?
        ): MatchToolTier {
            val tier = jsonObject.get("tier")
            if (tier == null) {
                ProgressionMod.LOGGER.error(
                    "Failed to parse lootable MatchToolTier predicate",
                    IllegalArgumentException("Missing tier key in MatchToolTier predicate")
                )
                return MatchToolTier(null, Condition.Equal)
            }
            val cond =
                jsonObject.get("cond") ?: throw IllegalArgumentException("Missing cond key in MatchToolTier predicate")
            val obj = fromString(tier.asString, cond.asString)
            return obj
        }
    }

    companion object {
        fun fromIdentifier(tier: Identifier, condition: Condition): MatchToolTier {
            val mat = TierSortingRegistry.byName(tier)
                ?: throw InvalidIdentifierException("$tier is not registered in TierSortingRegistry")
            return MatchToolTier(mat, condition)
        }

        fun fromString(belowTier: String, cond: String): MatchToolTier {
            val id = Identifier.tryParse(belowTier)
                ?: throw InvalidIdentifierException("$belowTier is not a valid identifier")
            val cond =
                Condition.fromJson(cond) ?: throw IllegalArgumentException("$belowTier is not a valid condition")
            return fromIdentifier(id, cond)
        }
    }
}
