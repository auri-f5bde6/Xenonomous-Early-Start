package com.github.auri_f5bde6.xeno_early_start.event

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStartTags
import com.github.auri_f5bde6.xeno_early_start.config.XenoEarlyStartConfig
import com.github.auri_f5bde6.xeno_early_start.item.PebbleItem
import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartItemRegistry
import net.minecraft.block.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraftforge.common.Tags
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber

@Suppress("UNUSED_PARAMETER")
@EventBusSubscriber(modid = XenoEarlyStart.MODID, bus = EventBusSubscriber.Bus.FORGE)
object LivingEntityUseItemEventHandler {
    @SubscribeEvent
    fun onUse(event: PlayerInteractEvent.RightClickBlock) {
        val playerEntity = event.entity
        val world = playerEntity.world
        val blockPos = event.pos
        val state = world.getBlockState(blockPos)
        if (event.itemStack.isOf(Items.FLINT) && PebbleItem.useOnBlock(
                world,
                blockPos,
                playerEntity,
                ItemStack(
                    XenoEarlyStartItemRegistry.FLINT_SHARD.get(),
                    world.getRandom().nextBetween(2, 5)
                )
            ) == ActionResult.SUCCESS
        ) {
            event.isCanceled = true
            event.cancellationResult = ActionResult.SUCCESS
        } else if (event.itemStack.isIn(Tags.Items.GRAVEL) && state.isOf(Blocks.GRINDSTONE)) {
            event.isCanceled = true
            event.itemStack.decrement(1)
            if (world.random.nextFloat() < 0.8) {
                playerEntity.giveItemStack(ItemStack(Items.FLINT))
                event.cancellationResult = ActionResult.SUCCESS
            } else {
                event.cancellationResult = ActionResult.FAIL
            }
        } else if (XenoEarlyStartConfig.config.earlyGameChanges.stationsUnusableUntilFirstCraft && playerEntity is ServerPlayerEntity && world is ServerWorld) {
            val isFurnaces = state.isIn(XenoEarlyStartTags.Blocks.FURNACES)
            val isCraftingTable = state.isIn(XenoEarlyStartTags.Blocks.CRAFTING_TABLES)
            val unlockedCraftingTable = playerEntity.advancementTracker.getProgress(
                world.server.advancementLoader.get(
                    XenoEarlyStart.of("crafting_table")
                )
            ).isDone
            val unlockedBrickFurnace = playerEntity.advancementTracker.getProgress(
                world.server.advancementLoader.get(
                    XenoEarlyStart.of("brick_furnace")
                )
            ).isDone
            if (isCraftingTable && !unlockedCraftingTable) {
                event.isCanceled = true
                event.cancellationResult = ActionResult.FAIL
                playerEntity.sendMessage(
                    Text.translatable("xeno_early_start.gameplay.require_crafting_table_advancement"),
                    true
                )
            } else if (isFurnaces && !unlockedBrickFurnace) {
                event.isCanceled = true
                event.cancellationResult = ActionResult.FAIL
                playerEntity.sendMessage(
                    Text.translatable("xeno_early_start.gameplay.require_furnace_advancement"),
                    true
                )
            }
        }

    }
}