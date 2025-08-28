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

class PickaxeTier(var lowerThanTier: ToolMaterial?) : LootCondition {
    override fun getType(): LootConditionType {
        return ProgressionModLootTypeRegistry.pickaxeTier.get()
    }

    override fun test(lootContext: LootContext): Boolean {
        val itemStack = lootContext.get(LootContextParameters.TOOL)
        return MiningLevel.IsToolLowerThanTier(itemStack, lowerThanTier)
    }

    class Serializer : JsonSerializer<PickaxeTier> {
        override fun toJson(
            jsonObject: JsonObject,
            pickaxeTier: PickaxeTier,
            jsonSerializationContext: JsonSerializationContext?
        ) {
            jsonObject.addProperty("below_tier", MiningLevel.getTierName(pickaxeTier.lowerThanTier))
        }

        override fun fromJson(
            jsonObject: JsonObject,
            jsonDeserializationContext: JsonDeserializationContext?
        ): PickaxeTier {
            return fromString(jsonObject.get("below_tier").asString)
        }
    }

    companion object {
        fun fromIdentifier(belowTierIdentifier: Identifier?): PickaxeTier {
            val mat = TierSortingRegistry.byName(belowTierIdentifier)
            if (mat == null) {
                ProgressionMod.Companion.LOGGER.error(
                    "{} is not registered in TierSortingRegistry",
                    belowTierIdentifier
                )
            }
            return PickaxeTier(mat)
        }

        fun fromString(belowTier: String?): PickaxeTier {
            val id = Identifier.tryParse(belowTier)
            if (id == null) {
                ProgressionMod.Companion.LOGGER.error(
                    "{} is not a valid identifier",
                    belowTier,
                    InvalidIdentifierException("$belowTier is not a valid identifier")
                )
            }
            return fromIdentifier(id)
        }
    }
}
