package net.hellomouse.xeno_early_start.item

import net.hellomouse.xeno_early_start.ProgressionMod
import net.minecraft.item.Item
import net.minecraft.item.ItemUsageContext
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraftforge.common.Tags

class PebbleItem(private val result: Item, settings: Settings) : Item(settings) {
    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        val world = context.world
        val blockPos = context.blockPos
        val blockState = world.getBlockState(blockPos)
        val player = context.player
        if (player != null) {
            val mainhandStack = player.mainHandStack
            val offhandStack = player.offHandStack
            if (offhandStack.isOf(mainhandStack.item) && offhandStack.item is PebbleItem && (blockState.isIn(
                    Tags.Blocks.COBBLESTONE
                ) || blockState.isIn(Tags.Blocks.STONE))
            ) {
                if (!player.isCreative){
                    mainhandStack.decrement(1)
                    offhandStack.decrement(1)
                    player.addExhaustion(4.5f)
                }
                player.giveItemStack(result.defaultStack)
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
            ProgressionMod.LOGGER.warn("RockItem useOnBlock's context have no player set")
            return ActionResult.FAIL
        }
    }
}
