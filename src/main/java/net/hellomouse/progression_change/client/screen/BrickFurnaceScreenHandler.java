package net.hellomouse.progression_change.client.screen;

import net.hellomouse.progression_change.registries.ProgressionModScreenHandlerRegistry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractFurnaceScreenHandler;
import net.minecraft.screen.PropertyDelegate;

public class BrickFurnaceScreenHandler extends AbstractFurnaceScreenHandler {
    public BrickFurnaceScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ProgressionModScreenHandlerRegistry.BRICK_FURNACE_SCREEN.get(), RecipeType.SMELTING, RecipeBookCategory.FURNACE, syncId, playerInventory);
    }

    public BrickFurnaceScreenHandler(
            int syncId,
            PlayerInventory playerInventory,
            Inventory inventory,
            PropertyDelegate propertyDelegate
    ) {
        super(ProgressionModScreenHandlerRegistry.BRICK_FURNACE_SCREEN.get(), RecipeType.SMELTING, RecipeBookCategory.FURNACE, syncId, playerInventory, inventory, propertyDelegate);

    }
}
