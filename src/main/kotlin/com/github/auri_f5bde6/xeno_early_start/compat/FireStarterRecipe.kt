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
import net.minecraft.registry.tag.ItemTags
import net.minecraft.util.Identifier
import net.minecraftforge.common.Tags

class FireStarterRecipe : EmiRecipe {
    override fun getCategory(): EmiRecipeCategory {
        return XenoEarlyStartEmiPlugin.FIRE_STARTER_CATEGORY
    }

    override fun getId(): Identifier {
        return XenoEarlyStart.of("/world_interaction")
    }

    override fun getInputs(): List<EmiIngredient> {
        // The _actual_ implementation check if item is burnable, but this should be an exhaustive enough list that no one will notice the inaccuracy
        // I hope
        return listOf(
            EmiIngredient.of(ItemTags.PLANKS), EmiIngredient.of(ItemTags.LOGS), EmiIngredient.of(
                Ingredient.ofItems(
                    Items.STICK,
                    Items.COAL, Items.CHARCOAL
                )
            ),
            EmiIngredient.of(Tags.Items.STORAGE_BLOCKS_COAL)
        )
    }

    override fun getOutputs(): List<EmiStack> {
        return listOf(EmiStack.of { XenoEarlyStartItemRegistry.PRIMITIVE_FIRE.get() })
    }

    override fun getDisplayWidth(): Int {
        return 76
    }

    override fun getDisplayHeight(): Int {
        return 18
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 25, 1)
        widgets.addGeneratedSlot({ random -> inputs[random.nextInt(inputs.size)] }, 1234, 0, 0)
        widgets.addSlot(outputs[0], 55, 0).recipeContext(this)
    }
}