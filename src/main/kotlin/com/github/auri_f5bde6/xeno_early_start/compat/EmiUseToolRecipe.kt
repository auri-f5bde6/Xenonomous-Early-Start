package com.github.auri_f5bde6.xeno_early_start.compat

import com.github.auri_f5bde6.xeno_early_start.recipe.UseToolRecipe
import dev.emi.emi.api.recipe.EmiCraftingRecipe
import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.Recipe

class EmiUseToolRecipe(val recipe: UseToolRecipe) : EmiCraftingRecipe(
    listOf(EmiIngredient.of(recipe.input)),
    EmiStack.of(recipe.output),
    recipe.id
) {
    val possibleTools = recipe.tool.matchingStacks

    companion object {
        private fun withRemainder(ingredient: Ingredient): EmiIngredient {
            if (ingredient.isEmpty) {
                return EmiStack.EMPTY
            }
            val emiIngredients = arrayListOf<EmiIngredient>()
            for (i in ingredient.matchingStacks) {
                val emiStack = EmiStack.of(i)
                val new = i.copy()
                new.damage += 1
                emiStack.setRemainder(EmiStack.of(new))
                emiIngredients.add(emiStack)
            }
            return EmiIngredient.of(ingredient, 1)
        }
    }

    override fun getBackingRecipe(): Recipe<*> {
        return recipe
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 60, 18)
        if (shapeless) {
            widgets.addTexture(EmiTexture.SHAPELESS, 97, 0)
        }
        var sOff = 0
        if (!shapeless) {
            if (canFit(1, 3)) {
                sOff -= 1
            }
            if (canFit(3, 1)) {
                sOff -= 3
            }
        }
        widgets.addGeneratedSlot({ random ->
            val tool = possibleTools[random.nextInt(0, possibleTools.size - 1)]
            tool.damage = random.nextInt(0, tool.maxDamage - recipe.toolDamage * 2)
            val original = EmiStack.of(tool.copy())
            tool.damage += recipe.toolDamage
            val new = EmiStack.of(tool)
            original.setRemainder(new)
            original
        }, 123, 0, 0)
        for (i in 0..7) {
            val s = i + sOff
            if (s >= 0 && s < input.size) {
                widgets.addSlot(input[s], (i + 1) % 3 * 18, (i + 1) / 3 * 18)
            } else {
                widgets.addSlot(EmiStack.of(ItemStack.EMPTY), (i + 1) % 3 * 18, (i + 1) / 3 * 18)
            }
        }
        widgets.addSlot(output, 92, 14).large(true).recipeContext(this)
    }

}