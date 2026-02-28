package com.github.auri_f5bde6.xeno_early_start.item

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStartTags
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class PebbleItem(private val result: Item, settings: Settings) : Item(settings) {
    companion object {
        fun useOnBlock(world: World, blockPos: BlockPos, player: PlayerEntity?, result: ItemStack): ActionResult {
            val blockState = world.getBlockState(blockPos)
            if (player != null) {
                val mainhandStack = player.mainHandStack
                val offhandStack = player.offHandStack
                if (blockState.isIn(XenoEarlyStartTags.Blocks.CAN_KNAP_STONE)) {
                    if (!player.isCreative) {
                        if (offhandStack.isOf(mainhandStack.item)) {
                            mainhandStack.decrement(1)
                            offhandStack.decrement(1)
                        } else if (mainhandStack.count >= 2) {
                            mainhandStack.decrement(2)
                        } else {
                            return ActionResult.FAIL
                        }

                        player.addExhaustion(4.5f)
                    }
                    player.giveItemStack(result)
                    world.playSound(
                        player,
                        blockPos,
                        SoundEvents.ITEM_FLINTANDSTEEL_USE,
                        SoundCategory.PLAYERS,
                        1.0f,
                        world.getRandom().nextFloat() * 0.4f + 0.8f
                    )
                    return ActionResult.SUCCESS
                } else {
                    return ActionResult.PASS
                }
            } else {
                return ActionResult.FAIL
            }
        }
    }

    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        return useOnBlock(context.world, context.blockPos, context.player, result.defaultStack)
    }
}
