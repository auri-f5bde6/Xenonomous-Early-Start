package com.github.auri_f5bde6.xeno_early_start.recipe

import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartRecipeRegistry
import com.github.auri_f5bde6.xeno_early_start.utils.JsonUtils
import com.google.gson.JsonObject
import net.minecraft.inventory.RecipeInputInventory
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.recipe.CraftingRecipe
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.book.CraftingRecipeCategory
import net.minecraft.registry.DynamicRegistryManager
import net.minecraft.util.Identifier
import net.minecraft.util.JsonHelper
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.random.Random
import net.minecraft.world.World
import net.minecraftforge.common.crafting.CraftingHelper

class UseToolRecipe(
    val recipeId: Identifier,
    val craftingRecipeCategory: CraftingRecipeCategory,
    val tool: Ingredient,
    val input: Ingredient,
    val output: ItemStack,
    val toolDamage: Int,
) :
    CraftingRecipe {
    companion object {
        private val RANDOM = Random.create()
    }

    override fun matches(
        inventory: RecipeInputInventory,
        world: World
    ): Boolean {
        var hasInput = false
        var hasTool = false
        for (i in inventory.inputStacks) {
            if (tool.test(i)) {
                if (hasTool) return false
                hasTool = true
            } else if (input.test(i)) {
                if (hasInput) return false
                hasInput = true
            }
            if (hasTool && hasInput) return true
        }
        return false
    }

    override fun craft(
        inventory: RecipeInputInventory,
        registryManager: DynamicRegistryManager
    ): ItemStack {
        return getOutput(registryManager).copy()
    }

    override fun getRemainder(inventory: RecipeInputInventory): DefaultedList<ItemStack> {
        val l = DefaultedList.ofSize(inventory.size(), ItemStack.EMPTY)
        for ((i, v) in inventory.inputStacks.withIndex()) {
            if (tool.test(v)) {
                val new = v.copy()
                val broken = new.damage(toolDamage, RANDOM, null)
                if (broken) {
                    new.decrement(1)
                    new.damage = 0
                }
                l[i] = new
            }
        }
        return l
    }

    override fun getIngredients(): DefaultedList<Ingredient> {
        return DefaultedList.copyOf(Ingredient.EMPTY, input, tool)
    }

    override fun fits(width: Int, height: Int): Boolean {
        return width >= 2 || height >= 2
    }

    override fun getOutput(registryManager: DynamicRegistryManager): ItemStack {
        return output
    }

    override fun getId(): Identifier {
        return recipeId
    }


    override fun getSerializer(): RecipeSerializer<UseToolRecipe> {
        return XenoEarlyStartRecipeRegistry.USE_TOOL.get()
    }

    override fun getCategory(): CraftingRecipeCategory {
        return craftingRecipeCategory
    }

    class Serializer : RecipeSerializer<UseToolRecipe> {
        override fun read(
            id: Identifier,
            json: JsonObject
        ): UseToolRecipe {
            val input = Ingredient.fromJson(json.get("input"), false)
            val output = CraftingHelper.getItemStack(json.get("output").asJsonObject, true, true)
            val tool = Ingredient.fromJson(json.get("tool"), false)
            val craftingBookCategory = CraftingRecipeCategory.CODEC
                .byId(JsonHelper.getString(json, "category", null as String?), CraftingRecipeCategory.MISC)
            val toolDamage = JsonUtils.getInt(json, "tool_damage") ?: 1
            return UseToolRecipe(id, craftingBookCategory, tool, input, output, toolDamage)
        }

        override fun read(
            id: Identifier,
            buf: PacketByteBuf
        ): UseToolRecipe {
            val input = Ingredient.fromPacket(buf)
            val output = buf.readItemStack()
            val tool = Ingredient.fromPacket(buf)
            val craftingBookCategory: CraftingRecipeCategory =
                buf.readEnumConstant(CraftingRecipeCategory::class.java)
            val toolDamage = buf.readInt()
            return UseToolRecipe(id, craftingBookCategory, tool, input, output, toolDamage)
        }

        override fun write(
            buf: PacketByteBuf,
            recipe: UseToolRecipe
        ) {
            recipe.input.write(buf)
            buf.writeItemStack(recipe.output)
            recipe.tool.write(buf)
            buf.writeString(recipe.toolDamage.toString())
            buf.writeEnumConstant(recipe.craftingRecipeCategory)
            buf.writeInt(recipe.toolDamage)

        }
    }
}