package com.github.auri_f5bde6.xeno_early_start.item

import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.recipe.RecipeType

class BurnableItem(settings: Settings, val burnTime: Int) : Item(settings) {
    override fun getBurnTime(itemStack: ItemStack?, recipeType: RecipeType<*>?): Int {
        return burnTime
    }
}