package com.github.auri_f5bde6.xeno_early_start.item

import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartSoundEventRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsage
import net.minecraft.item.Items
import net.minecraft.sound.SoundCategory
import net.minecraft.stat.Stats
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.hit.HitResult
import net.minecraft.world.RaycastContext
import net.minecraft.world.World

class DryClayDustItem(settings: Settings) : Item(settings) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val itemStack = user.getStackInHand(hand)
        val result = raycast(world, user, RaycastContext.FluidHandling.WATER)
        if (result.type != HitResult.Type.MISS) {
            user.increaseStat(Stats.USED.getOrCreateStat(this), 1)
            world.playSound(
                null,
                user.blockPos,
                XenoEarlyStartSoundEventRegistry.DUST_TURNS_TO_CLAY.get(),
                SoundCategory.NEUTRAL,
                1f,
                1f
            )
            val remainingStack =
                ItemUsage.exchangeStack(itemStack, user, Items.CLAY_BALL.defaultStack)
            return TypedActionResult.success(remainingStack, world.isClient)
        }
        return TypedActionResult.pass(itemStack)
    }
}