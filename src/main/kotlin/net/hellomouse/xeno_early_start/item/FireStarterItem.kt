package net.hellomouse.xeno_early_start.item

import net.hellomouse.xeno_early_start.block.PrimitiveFIreBlock
import net.hellomouse.xeno_early_start.registries.ProgressionModBlockRegistry
import net.minecraft.entity.ItemEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemUsageContext
import net.minecraft.recipe.RecipeType
import net.minecraft.util.ActionResult
import net.minecraft.util.math.Box

class FireStarterItem(settings: Settings) : Item(settings) {
    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        val world = context.world
        val aboveBlock = context.blockPos.add(0, 1, 0)
        val box = Box(aboveBlock, aboveBlock.add(1, 1, 1))
        val items = world.getEntitiesByClass(ItemEntity::class.java, box) { true }
        val burnables = mutableListOf<ItemEntity>()
        var burnablesTotal = 0
        for (item in items) {
            if (getBurnTime(item.stack, RecipeType.SMELTING) != 0) {
                burnablesTotal += item.stack.count
                burnables.add(item)
            } else {
                burnablesTotal = 0
                break
            }
        }
        if (burnablesTotal == 5) {
            for (item in burnables) {
                var blockState = ProgressionModBlockRegistry.PRIMITIVE_FIRE.get().defaultState
                item.kill()
                val player = context.player
                if (player != null) {
                    blockState = blockState.with(
                        PrimitiveFIreBlock.FACING, player.horizontalFacing
                    )
                }
                world.setBlockState(aboveBlock, blockState)
            }
            return ActionResult.SUCCESS
        } else {
            return ActionResult.FAIL
        }
    }
}