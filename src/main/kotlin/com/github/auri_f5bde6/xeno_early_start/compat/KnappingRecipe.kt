package com.github.auri_f5bde6.xeno_early_start.compat

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import net.minecraft.item.Item
import net.minecraft.recipe.Ingredient
import net.minecraft.util.Identifier
import net.minecraft.util.math.RotationAxis
import net.minecraftforge.common.Tags
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class KnappingRecipe(val pebble: Item, val result: Item) : EmiRecipe {
    val input = listOf(EmiIngredient.of(Ingredient.ofItems(pebble)), EmiIngredient.of(Ingredient.ofItems(pebble)))
    val output = listOf(EmiStack.of(result))
    override fun getCategory(): EmiRecipeCategory {
        return XenoEarlyStartEmiPlugin.KNAPPING
    }

    override fun getId(): Identifier {
        return XenoEarlyStart.of("/knapping")
    }

    override fun getInputs(): List<EmiIngredient> {
        return input
    }

    override fun getOutputs(): List<EmiStack> {
        return output
    }

    override fun getDisplayWidth(): Int {
        return 76
    }

    override fun getDisplayHeight(): Int {
        return 105
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addSlot(inputs[0], 0, 0)
        widgets.addSlot(inputs[1], 58, 0)
        widgets.addSlot(EmiIngredient.of(Tags.Items.COBBLESTONE), 29, 43).recipeContext(this)
        addRotatedTexture(widgets, EmiTexture.EMPTY_ARROW, 11, 17, 65f)
        addRotatedTexture(widgets, EmiTexture.EMPTY_ARROW, 43, 27, 115f)
        addRotatedTexture(widgets, EmiTexture.EMPTY_ARROW, 26, 67, 90f)
        widgets.addSlot(EmiIngredient.of(Ingredient.ofItems(result)), 29, 88).recipeContext(this)

    }

    fun addRotatedTexture(widgets: WidgetHolder, texture: EmiTexture, x: Int, y: Int, deg: Float) {
        val rad = deg / 360 * 2 * PI
        val newHeight = texture.width * cos(rad) + texture.height * sin(rad)
        val newWidth = texture.height * cos(rad) + texture.width * sin(rad)
        widgets.addDrawable(
            x,
            y, newWidth.toInt(), newHeight.toInt()
        ) { drawContext, _, _, delta ->
            drawContext.matrices.push()
            drawContext.matrices.translate(
                newWidth / 2.0,
                newHeight / 2.0,
                0.0
            )
            drawContext.matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(deg))
            drawContext.matrices.translate(-texture.width / 2.0, -texture.height / 2.0, 0.0)
            drawContext.drawTexture(texture.texture, 0, 0, texture.u, texture.v, texture.width, texture.height)
            drawContext.matrices.pop()
        }
    }
}