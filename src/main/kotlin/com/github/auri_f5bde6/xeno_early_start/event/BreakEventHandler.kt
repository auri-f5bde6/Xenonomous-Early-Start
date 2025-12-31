package com.github.auri_f5bde6.xeno_early_start.event

import com.github.auri_f5bde6.xeno_early_start.CoalDust.tryDetonate
import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStartTags
import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartRecipeRegistry
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraftforge.common.Tags
import net.minecraftforge.event.ForgeEventFactory
import net.minecraftforge.event.level.BlockEvent.BreakEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber

@Suppress("UNUSED_PARAMETER")
@EventBusSubscriber(modid = XenoEarlyStart.MODID, bus = EventBusSubscriber.Bus.FORGE)
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
            if (state.isIn(XenoEarlyStartTags.Blocks.HAS_BLOCK_TO_BLOCK_RECIPE)) {
                val recipes = (level as World).recipeManager
                    .listAllOfType(XenoEarlyStartRecipeRegistry.BLOCK_TO_BLOCK_TYPE.get())
                for (recipe in recipes) {
                    if (recipe.matches(state, toolStack)) {
                        breakBlock(toolStack, level, player, state, pos, recipe.isDropBlockLootTable)
                        recipe.maybeDropItemsInList(level, pos)
                        level.setBlockState(pos, recipe.resultingBlock.defaultState)
                        event.isCanceled = true
                        return
                    }
                }
            }
        }
        if (state.isIn(Tags.Blocks.ORES_COAL) && !level.isClient) {
            tryDetonate(level as ServerWorld, pos, false, null)
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
