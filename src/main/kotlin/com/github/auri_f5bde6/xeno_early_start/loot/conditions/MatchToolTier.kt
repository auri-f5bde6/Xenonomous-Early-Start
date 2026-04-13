package com.github.auri_f5bde6.xeno_early_start.loot.conditions

import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartLootTypeRegistry
import com.github.auri_f5bde6.xeno_early_start.utils.CodecJsonSerializer
import com.github.auri_f5bde6.xeno_early_start.utils.CodecUtils
import com.github.auri_f5bde6.xeno_early_start.utils.MiningLevel
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.item.ItemStack
import net.minecraft.item.ToolMaterial
import net.minecraft.loot.condition.LootCondition
import net.minecraft.loot.condition.LootConditionType
import net.minecraft.loot.context.LootContext
import net.minecraft.loot.context.LootContextParameters
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Identifier
import net.minecraft.util.JsonSerializer
import net.minecraftforge.common.TierSortingRegistry

open class MatchToolTier(val tier: ToolMaterial, val cond: Condition) : LootCondition {
    enum class Condition {
        Greater,
        GreaterEqual,
        Lesser,
        LessEqual,
        Equal,
        NotEqual;

        companion object {
            val CODEC: Codec<Condition> = Codec.STRING.comapFlatMap({ string ->
                CodecUtils.dataResultFromNullable(fromJson(string)) { "$string is not a valid condition" }
            }, { condition -> condition.toJson() })

            fun fromJson(string: String): Condition? {
                when (string.lowercase()) {
                    "greater", ">" -> return Greater
                    "greaterEqual", ">=" -> return GreaterEqual
                    "lesser", "<" -> return Lesser
                    "equal", "==" -> return Equal
                    "notEqual", "!=" -> return NotEqual
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
                NotEqual -> "!="
            }
        }
    }


    fun test(itemStack: ItemStack?): Boolean {
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

            Condition.NotEqual -> {
                !MiningLevel.isToolEqualToTier(itemStack, tier)
            }
        }
    }

    override fun test(lootContext: LootContext): Boolean {
        val itemStack = lootContext.get(LootContextParameters.TOOL)
        return test(itemStack)
    }

    override fun getType(): LootConditionType {
        return XenoEarlyStartLootTypeRegistry.matchToolTier.get()
    }

    class Serializer :
        JsonSerializer<MatchToolTier> by CodecJsonSerializer(CODEC.codec())
    companion object {
        val CODEC: MapCodec<MatchToolTier> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Identifier.CODEC.flatXmap(
                    { identifier ->
                        CodecUtils.dataResultFromNullable(TierSortingRegistry.byName(identifier)) { "$identifier is not registered in TierSortingRegistry" }
                    },
                    { material ->
                        CodecUtils.dataResultFromNullable(TierSortingRegistry.getName(material)) { "$material is not registered in TierSortingRegistry" }
                    })
                    .fieldOf("tier").forGetter(MatchToolTier::tier),
                Condition.CODEC.fieldOf("cond").forGetter(MatchToolTier::cond)
            ).apply(instance, ::MatchToolTier)
        }

        fun fromBuf(buf: PacketByteBuf): MatchToolTier {
            val tier = TierSortingRegistry.byName(buf.readIdentifier())!!
            val cond = buf.readEnumConstant(Condition::class.java)
            return MatchToolTier(tier, cond)
        }
    }

    fun toBuf(buf: PacketByteBuf) {
        buf.writeIdentifier(TierSortingRegistry.getName(tier))
        buf.writeEnumConstant(cond)
    }
}