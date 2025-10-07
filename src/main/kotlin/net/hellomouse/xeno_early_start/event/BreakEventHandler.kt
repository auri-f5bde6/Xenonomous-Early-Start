package net.hellomouse.xeno_early_start.event

import net.hellomouse.xeno_early_start.ProgressionMod
import net.hellomouse.xeno_early_start.ProgressionModTags
import net.hellomouse.xeno_early_start.recipe.StoneToCobbleRecipe
import net.hellomouse.xeno_early_start.registries.ProgressionModRecipeRegistry
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.AxeItem
import net.minecraft.item.ItemStack
import net.minecraft.registry.tag.BlockTags
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraftforge.event.ForgeEventFactory
import net.minecraftforge.event.level.BlockEvent.BreakEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber

@Suppress("UNUSED_PARAMETER")
@EventBusSubscriber(modid = ProgressionMod.MODID, bus = EventBusSubscriber.Bus.FORGE)
object BreakEventHandler {
    @SubscribeEvent
    fun onBreakEvent(event: BreakEvent) {
        val eventState = event.state
        val level = event.level
        val pos = event.pos
        val player = event.player
        val toolStack = player.mainHandStack
        val state = event.state
        if (eventState.canHarvestBlock(level, pos, player)) {
            if (state.isIn(ProgressionModTags.Blocks.HAS_BLOCK_TO_BLOCK_RECIPE)) {
                val recipes = (level as World).recipeManager
                    .listAllOfType(ProgressionModRecipeRegistry.BLOCK_TO_BLOCK_TYPE.get())
                for (recipe in recipes) {
                    if (recipe.matches(state, toolStack)) {
                        breakBlock(toolStack, level, player, state, pos, recipe.isDropBlockLootTable)
                        recipe.maybeDropItemsInList(level, pos)
                        level.setBlockState(pos, recipe.resultingBlock.defaultState)
                        event.setCanceled(true)
                        return
                    }
                }
            }
        }
    }

    private fun breakBlock(
        toolStack: ItemStack,
        level: WorldAccess,
        player: PlayerEntity?,
        blockState: BlockState,
        pos: BlockPos,
        drop: Boolean = false
    ) {
        val toolstack1 = toolStack.copy()
        level.breakBlock(pos, false, player)
        blockState.block.onBroken(level, pos, blockState)
        toolStack.postMine(level as World, blockState, pos, player)
        if (toolStack.isEmpty && !toolstack1.isEmpty) {
            ForgeEventFactory.onPlayerDestroyItem(player, toolstack1, Hand.MAIN_HAND)
        }
        if (drop) {
            val blockEntity = level.getBlockEntity(pos)
            blockState.block.afterBreak(level, player, pos, blockState, blockEntity, toolstack1)
        }
    }
}
