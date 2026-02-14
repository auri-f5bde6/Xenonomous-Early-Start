package com.github.auri_f5bde6.xeno_early_start.event

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.github.auri_f5bde6.xeno_early_start.item.PebbleItem
import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartItemRegistry
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
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
        if (event.itemStack.isOf(Items.FLINT)) {
            event.isCanceled = true
            event.cancellationResult = PebbleItem.useOnBlock(
                world,
                blockPos,
                playerEntity,
                ItemStack(
                    XenoEarlyStartItemRegistry.FLINT_SHARD.get(),
                    world.getRandom().nextBetween(2, 5)
                )
            )

        }
    }
}