package com.github.auri_f5bde6.xeno_early_start.compat

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartItemRegistry
import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories
import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import net.minecraft.registry.tag.BlockTags
import net.minecraft.util.Identifier

class PlantFiberRecipe : EmiRecipe {
    override fun getCategory(): EmiRecipeCategory {
        return VanillaEmiRecipeCategories.WORLD_INTERACTION
    }

    override fun getId(): Identifier {
        return XenoEarlyStart.of("/plant_fiber")
    }

    override fun getInputs(): List<EmiIngredient> {
        return listOf(EmiIngredient.of(BlockTags.REPLACEABLE_BY_TREES))
    }

    override fun getOutputs(): List<EmiStack> {
        return listOf(EmiStack.of(XenoEarlyStartItemRegistry.PLANT_FIBER.get()))
    }

    override fun getDisplayWidth(): Int {
        return 76
    }

    override fun getDisplayHeight(): Int {
        return 18
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 25, 1)
        widgets.addSlot(inputs[0], 0, 0)
        widgets.addSlot(outputs[0], 55, 0).recipeContext(this)
    }
}