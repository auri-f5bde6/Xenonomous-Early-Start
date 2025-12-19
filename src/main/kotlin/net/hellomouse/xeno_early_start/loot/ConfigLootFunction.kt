package net.hellomouse.xeno_early_start.loot

import com.google.gson.*
import com.google.gson.annotations.SerializedName
import net.hellomouse.xeno_early_start.ProgressionMod
import net.hellomouse.xeno_early_start.ProgressionModConfig
import net.hellomouse.xeno_early_start.registries.ProgressionModLootTypeRegistry
import net.minecraft.item.ItemStack
import net.minecraft.loot.context.LootContext
import net.minecraft.loot.function.LootFunction
import net.minecraft.loot.function.LootFunctionType
import net.minecraft.util.JsonSerializer
import net.minecraft.util.math.MathHelper

/** Make it so the loot table drop amount of raw nuggets as configured in UI */
class ConfigLootFunction private constructor(
    private val dropType: DropType
) : LootFunction {

    override fun getType(): LootFunctionType {
        return ProgressionModLootTypeRegistry.configLootFunction.get()
    }

    override fun apply(itemStack: ItemStack, lootContext: LootContext): ItemStack {
        when (dropType) {
            DropType.Copper -> {
                if (ProgressionModConfig.config.oreChanges.rawCopperNuggetDropMax < ProgressionModConfig.config.oreChanges.rawCopperNuggetDropMin) {
                    ProgressionMod.LOGGER.error("rawCopperNuggetDropMin cannot be less than rawCopperNuggetDropMax, please check your mod config")
                }
                itemStack.count = MathHelper.nextInt(
                    lootContext.random,
                    ProgressionModConfig.config.oreChanges.rawCopperNuggetDropMin,
                    ProgressionModConfig.config.oreChanges.rawCopperNuggetDropMax
                )
            }

            DropType.Iron -> {
                itemStack.count = ProgressionModConfig.config.oreChanges.rawIronNuggetDrop
            }

            DropType.Gold -> {
                itemStack.count = ProgressionModConfig.config.oreChanges.rawGoldNuggetDrop
            }

            DropType.Diamond -> {
                itemStack.count = ProgressionModConfig.config.oreChanges.diamondFragmentDrop
            }
        }
        return itemStack
    }

    internal enum class DropType {
        @SerializedName("iron")
        Iron,

        @SerializedName("copper")
        Copper,

        @SerializedName("gold")
        Gold,

        @SerializedName("diamond")
        Diamond
    }

    class Serializer : JsonSerializer<ConfigLootFunction> {
        override fun toJson(json: JsonObject, `object`: ConfigLootFunction, context: JsonSerializationContext) {
            json.addProperty("drop_type", GSON.toJson(`object`.dropType))
        }

        override fun fromJson(json: JsonObject, context: JsonDeserializationContext?): ConfigLootFunction {
            return ConfigLootFunction(
                GSON.fromJson(json.get("drop_type"), DropType::class.java)
            )
        }
    }

    companion object {
        private val GSON: Gson = GsonBuilder().create()
    }
}
