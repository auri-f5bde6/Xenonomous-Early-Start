package com.github.auri_f5bde6.xeno_early_start.compat

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartItemRegistry
import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import net.minecraft.item.Items
import net.minecraft.recipe.Ingredient
import net.minecraft.util.Identifier

class DryingEmiRecipe : EmiRecipe {
    override fun getCategory(): EmiRecipeCategory {
        return XenoEarlyStartEmiPlugin.DRYING
    }

    override fun getId(): Identifier {
        return XenoEarlyStart.of("/drying")
    }

    override fun getInputs(): List<EmiIngredient> {
        return listOf(EmiIngredient.of(Ingredient.ofItems(XenoEarlyStartItemRegistry.RAW_BRICK.get())))
    }

    override fun getOutputs(): List<EmiStack> {
        return listOf(EmiStack.of { Items.BRICK })
    }

    override fun getDisplayWidth(): Int {
        return 76
    }

    override fun getDisplayHeight(): Int {
        return 18
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 26, 1)
        widgets.addSlot(inputs[0], 0, 0)
        widgets.addSlot(outputs[0], 58, 0).recipeContext(this)
    }
}