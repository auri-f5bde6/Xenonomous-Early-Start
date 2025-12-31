package com.github.auri_f5bde6.xeno_early_start.client.screen

import net.minecraft.client.gui.screen.recipebook.FurnaceRecipeBookScreen
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import net.minecraft.util.Identifier

class BrickFurnaceScreen(handler: BrickFurnaceScreenHandler, inventory: PlayerInventory, title: Text) :
    net.minecraft.client.gui.screen.ingame.AbstractFurnaceScreen<BrickFurnaceScreenHandler>(
        handler,
        FurnaceRecipeBookScreen(),
        inventory,
        title,
        TEXTURE
    ) {
    companion object {
        private val TEXTURE = Identifier.of("minecraft", "textures/gui/container/furnace.png")
    }
}
