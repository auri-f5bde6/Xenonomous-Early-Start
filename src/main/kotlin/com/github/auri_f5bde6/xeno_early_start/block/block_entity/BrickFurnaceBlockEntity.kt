package com.github.auri_f5bde6.xeno_early_start.block.block_entity

import com.github.auri_f5bde6.xeno_early_start.client.screen.BrickFurnaceScreenHandler
import com.github.auri_f5bde6.xeno_early_start.registries.ProgressionModBlockEntityRegistry
import net.minecraft.block.BlockState
import net.minecraft.block.entity.AbstractFurnaceBlockEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.recipe.RecipeType
import net.minecraft.screen.ScreenHandler
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos

class BrickFurnaceBlockEntity(pos: BlockPos, state: BlockState) :
    AbstractFurnaceBlockEntity(ProgressionModBlockEntityRegistry.BRICK_FURNACE.get(), pos, state, RecipeType.SMELTING) {
    override fun getContainerName(): Text {
        return Text.translatable("container.xeno_early_start.brick_furnace")
    }

    override fun createScreenHandler(syncId: Int, playerInventory: PlayerInventory): ScreenHandler {
        return BrickFurnaceScreenHandler(syncId, playerInventory, this, this.propertyDelegate)
    }

}
