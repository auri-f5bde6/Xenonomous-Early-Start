package net.hellomouse.xeno_early_start.client.screen

import net.hellomouse.xeno_early_start.registries.ProgressionModScreenHandlerRegistry
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.book.RecipeBookCategory
import net.minecraft.screen.AbstractFurnaceScreenHandler
import net.minecraft.screen.PropertyDelegate

class BrickFurnaceScreenHandler : AbstractFurnaceScreenHandler {
    constructor(
        syncId: Int,
        playerInventory: PlayerInventory
    ) : super(
        ProgressionModScreenHandlerRegistry.BRICK_FURNACE_SCREEN.get(),
        RecipeType.SMELTING,
        RecipeBookCategory.FURNACE,
        syncId,
        playerInventory
    )

    constructor(
        syncId: Int,
        playerInventory: PlayerInventory,
        inventory: Inventory,
        propertyDelegate: PropertyDelegate
    ) : super(
        ProgressionModScreenHandlerRegistry.BRICK_FURNACE_SCREEN.get(),
        RecipeType.SMELTING,
        RecipeBookCategory.FURNACE,
        syncId,
        playerInventory,
        inventory,
        propertyDelegate
    )
}
