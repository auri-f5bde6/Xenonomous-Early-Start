package com.github.auri_f5bde6.xeno_early_start.loot

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStartConfig
import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartLootTypeRegistry
import com.google.gson.*
import com.google.gson.annotations.SerializedName
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
        return XenoEarlyStartLootTypeRegistry.configLootFunction.get()
    }

    override fun apply(itemStack: ItemStack, lootContext: LootContext): ItemStack {
        when (dropType) {
            DropType.Copper -> {
                if (XenoEarlyStartConfig.config.oreChanges.rawCopperNuggetDropMax < XenoEarlyStartConfig.config.oreChanges.rawCopperNuggetDropMin) {
                    XenoEarlyStart.LOGGER.error("rawCopperNuggetDropMin cannot be less than rawCopperNuggetDropMax, please check your mod config")
                }
                itemStack.count = MathHelper.nextInt(
                    lootContext.random,
                    XenoEarlyStartConfig.config.oreChanges.rawCopperNuggetDropMin,
                    XenoEarlyStartConfig.config.oreChanges.rawCopperNuggetDropMax
                )
            }

            DropType.Iron -> {
                itemStack.count = XenoEarlyStartConfig.config.oreChanges.rawIronNuggetDrop
            }

            DropType.Gold -> {
                itemStack.count = XenoEarlyStartConfig.config.oreChanges.rawGoldNuggetDrop
            }

            DropType.Diamond -> {
                itemStack.count = XenoEarlyStartConfig.config.oreChanges.diamondFragmentDrop
            }

            DropType.PlantFiber -> {
                if (lootContext.random.nextFloat() < XenoEarlyStartConfig.config.earlyGameChanges.plantFiberDropProbability) {
                    itemStack.count = 1
                } else {
                    itemStack.count = 0
                }
            }

            DropType.Pebble -> {
                val prob = if (XenoEarlyStartConfig.config.earlyGameChanges.overridePebbleDropProbability) {
                    XenoEarlyStartConfig.config.earlyGameChanges.pebbleDropProbability
                } else {
                    0.4f
                }
                if (lootContext.random.nextFloat() < prob) {
                    itemStack.count += 1
                }
                if (lootContext.random.nextFloat() < prob) {
                    itemStack.count += 1
                }
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
        Diamond,

        @SerializedName("plant_fiber")
        PlantFiber,

        @SerializedName("pebble")
        Pebble
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
