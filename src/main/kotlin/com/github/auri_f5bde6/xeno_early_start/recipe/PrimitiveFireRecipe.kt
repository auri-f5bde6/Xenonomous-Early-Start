package com.github.auri_f5bde6.xeno_early_start.recipe

import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartBlockRegistry
import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartRecipeRegistry
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.book.CookingRecipeCategory
import net.minecraft.util.Identifier

class PrimitiveFireRecipe(
    id: Identifier, group: String, category: CookingRecipeCategory,
    input: Ingredient, output: ItemStack, experience: Float, cookTime: Int
) : net.minecraft.recipe.AbstractCookingRecipe(
    XenoEarlyStartRecipeRegistry.PRIMITIVE_FIRE_TYPE.get(), id,
    group,
    category, input, output, experience, cookTime
) {
    override fun getSerializer(): RecipeSerializer<*> {
        return XenoEarlyStartRecipeRegistry.PRIMITIVE_FIRE.get()
    }

    override fun createIcon(): ItemStack {
        return ItemStack(XenoEarlyStartBlockRegistry.PRIMITIVE_FIRE.get())
    }

}