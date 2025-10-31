package net.hellomouse.xeno_early_start.event

import net.hellomouse.xeno_early_start.ProgressionMod
import net.hellomouse.xeno_early_start.ProgressionModTags
import net.hellomouse.xeno_early_start.registries.ProgressionModBlockRegistry
import net.hellomouse.xeno_early_start.registries.ProgressionModRecipeRegistry
import net.hellomouse.xeno_early_start.registries.XenoProgressionModParticleRegistry
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
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
                        event.isCanceled = true
                        return
                    }
                }
            }
        }
        if (state.isIn(Tags.Blocks.ORES_COAL) && !level.isClient) {
            repeat(100) {
                (level as ServerWorld).spawnParticles(
                    XenoProgressionModParticleRegistry.COAL_DUST.get(),
                    pos.x - 0.1 + level.random.nextFloat() * 1.1,
                    pos.y - 0.1 + level.random.nextFloat() * 1.5,
                    pos.z - 0.1 + level.random.nextFloat() * 1.1,
                    1,
                    level.random.nextFloat() * 1.5,
                    -0.01,
                    level.random.nextFloat() * 1.5,
                    0.005
                )
            }
            val range = 5
            for (i in -range..range) {
                for (j in -range..range) {
                    for (k in -range..range) {
                        val testPos = pos.add(i, j, k)
                        val b = level.getBlockState(testPos)
                        if (b.isOf(Blocks.TORCH) || b.isOf(Blocks.WALL_TORCH) || b.isOf(Blocks.CAMPFIRE) || b.isOf(
                                ProgressionModBlockRegistry.PRIMITIVE_FIRE.get()
                            )
                        ) {
                            (level as ServerWorld).createExplosion(
                                null,
                                testPos.x.toDouble(),
                                testPos.y.toDouble(),
                                testPos.z.toDouble(),
                                2f,
                                true,
                                World.ExplosionSourceType.NONE
                            )
                        }
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
