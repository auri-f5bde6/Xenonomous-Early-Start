package com.github.auri_f5bde6.xeno_early_start.compat

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStartConfig
import com.github.auri_f5bde6.xeno_early_start.recipe.StoneToCobbleRecipe
import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import net.minecraft.client.MinecraftClient
import net.minecraft.recipe.Ingredient
import net.minecraft.registry.tag.TagKey
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraftforge.registries.ForgeRegistries

class EMIBlockToBlockRecipe(val recipe: StoneToCobbleRecipe) : EmiRecipe {
    val output: MutableList<EmiStack> = mutableListOf()
    val input: List<EmiIngredient>

    init {
        output.add(EmiStack.of(recipe.resultingBlock.asItem()))
        for (i in recipe.droppedItems) {
            val stack = EmiStack.of(i.item)
            stack.chance = i.probability
            output.add(stack)
        }
        input = if (recipe.minedBlockIsTag) {
            listOf(EmiIngredient.of(TagKey.of(ForgeRegistries.ITEMS.registryKey, recipe.minedBlock)))
        } else {
            listOf(EmiIngredient.of(Ingredient.ofItems(ForgeRegistries.ITEMS.getValue(recipe.minedBlock)!!)))
        }
    }

    override fun getCategory(): EmiRecipeCategory {
        return XenoEarlyStartEmiPlugin.BLOCK_TO_BLOCK
    }

    override fun getId(): Identifier {
        return recipe.id
    }

    override fun getInputs(): List<EmiIngredient?> {
        return input
    }

    override fun getOutputs(): List<EmiStack> {
        return output
    }

    override fun getDisplayWidth(): Int {
        return 94
    }

    override fun getDisplayHeight(): Int {
        return 18 + 1 + (recipe.droppedItems.lastIndex.floorDiv(4) + 1) * 19 + if (recipe.isDropBlockLootTable)
            MinecraftClient.getInstance().textRenderer.fontHeight - 1 else 0
    }

    override fun hideCraftable(): Boolean {
        return !(recipe.isOreToStone && XenoEarlyStartConfig.config.oreChanges.oreToStone || !recipe.isOreToStone)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        if (recipe.isOreToStone && XenoEarlyStartConfig.config.oreChanges.oreToStone || !recipe.isOreToStone) {
            val startX = 9

            widgets.addTexture(EmiTexture.EMPTY_ARROW, 26 + startX, 1)
            widgets.addSlot(input[0], 0 + startX, 0)
            widgets.addSlot(output[0], 58 + startX, 0).recipeContext(this)
            var startY = 19
            if (recipe.isDropBlockLootTable) {
                widgets.addText(
                    Text.translatable("emi.category.xeno_early_start.block_to_block.drop_loot_table"),
                    0,
                    startY,
                    -1,
                    true
                )
                startY += MinecraftClient.getInstance().textRenderer.fontHeight
            }
            for (i in 1..outputs.lastIndex) {
                val x = i % 4 - 1
                val y = i.floorDiv(4)
                output[i].chance = recipe.droppedItems[i - 1].probability
                widgets.addSlot(outputs[i], startX + x * 18, startY + 19 * y).recipeContext(this)
            }
        } else {
            widgets.addText(
                Text.translatable("emi.category.xeno_early_start.block_to_block.ore_to_stone_disabled"),
                0,
                displayHeight / 2 - 2,
                -1,
                true
            )
        }

    }
}