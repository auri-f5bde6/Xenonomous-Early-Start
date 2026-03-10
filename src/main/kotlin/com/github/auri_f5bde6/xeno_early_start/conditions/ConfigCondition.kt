package com.github.auri_f5bde6.xeno_early_start.conditions

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.github.auri_f5bde6.xeno_early_start.config.XenoEarlyStartConfig
import com.google.gson.JsonObject
import net.minecraft.util.Identifier
import net.minecraftforge.common.crafting.conditions.ICondition
import net.minecraftforge.common.crafting.conditions.IConditionSerializer
import kotlin.reflect.KProperty0

class ConfigCondition(val type: Type) : ICondition {
    enum class Type(val literal: String, val config: KProperty0<Boolean>) {
        HARD_BRICK_FURNACE(
            "harder_brick_furnace_recipe",
            XenoEarlyStartConfig.config.earlyGameChanges.recipes::harderBrickFurnaceRecipe
        ),
        VANILLA_FURNACE_RECIPE(
            "vanilla_furnace_recipe",
            XenoEarlyStartConfig.config.earlyGameChanges.recipes::vanillaFurnaceRecipe
        ),
        VANILLA_STONE_TOOL_RECIPE(
            "vanilla_stone_tool_recipe",
            XenoEarlyStartConfig.config.earlyGameChanges.recipes::vanillaStoneToolRecipe
        ),
        VANILLA_CRAFTING_TABLE_RECIPE(
            "vanilla_crafting_table_recipe",
            XenoEarlyStartConfig.config.earlyGameChanges.recipes::vanillaCraftingTableRecipe
        ), ;

        fun test(): Boolean {
            return this.config.get()
        }

        companion object {
            fun fromString(type: String) = Type.entries.find { v -> v.literal == type }
        }
    }

    companion object {
        val ID = XenoEarlyStart.of("config")
        val SERIALIZER = Serializer()
    }

    override fun getID(): Identifier {
        return ID
    }

    override fun test(iContext: ICondition.IContext): Boolean {
        return type.test()
    }

    class Serializer : IConditionSerializer<ConfigCondition> {
        override fun write(
            jsonObject: JsonObject,
            iCondition: ConfigCondition
        ) {
            jsonObject.addProperty("config", iCondition.type.literal)
        }

        override fun read(jsonObject: JsonObject): ConfigCondition {
            val c = jsonObject.get("config")
                ?: throw IllegalArgumentException("config key does not exist for xeno_early_start:config condition")
            return ConfigCondition(
                Type.fromString(c.asString)
                    ?: throw IllegalArgumentException("${c.asString} config type does not exist for xeno_early_start:config condition")
            )
        }

        override fun getID(): Identifier {
            return ID
        }

    }
}