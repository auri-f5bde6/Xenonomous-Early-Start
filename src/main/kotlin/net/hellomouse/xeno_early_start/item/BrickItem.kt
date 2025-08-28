package net.hellomouse.xeno_early_start.item

import net.hellomouse.xeno_early_start.entity.BrickEntity
import net.minecraft.block.Block
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.item.Vanishable
import net.minecraft.stat.Stats
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.UseAction
import net.minecraft.world.World

class BrickItem(block: Block, settings: Settings) : BlockItem(block, settings), Vanishable {
    override fun getMaxUseTime(stack: ItemStack?): Int {
        return 72000
    }

    override fun getUseAction(stack: ItemStack?): UseAction {
        return UseAction.BOW
    }

    override fun use(world: World?, user: PlayerEntity, hand: Hand?): TypedActionResult<ItemStack?> {
        val itemStack = user.getStackInHand(hand)
        user.setCurrentHand(hand)
        return TypedActionResult.consume<ItemStack?>(itemStack)
    }

    override fun postHit(stack: ItemStack, target: LivingEntity?, attacker: LivingEntity): Boolean {
        stack.damage(
            1,
            attacker
        ) { e: LivingEntity? -> e!!.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND) }
        return true
    }

    override fun onStoppedUsing(stack: ItemStack, world: World, user: LivingEntity?, remainingUseTicks: Int) {
        val i = this.getMaxUseTime(stack) - remainingUseTicks
        val progress: Float = getPullProgress(i)
        if (user is PlayerEntity) {
            val brickEntity = BrickEntity(world, user, ItemStack(stack.item))
            brickEntity.setVelocity(user, user.pitch, user.yaw, 0.0f, 2.5f * progress, 1.0f)
            world.spawnEntity(brickEntity)
            if (!user.abilities.creativeMode) {
                stack.decrement(1)
            }
            user.incrementStat(Stats.USED.getOrCreateStat(this))
        }
    }

    companion object {
        fun getPullProgress(useTicks: Int): Float {
            var f = useTicks / 20.0f
            f = (f * f + f * 2.0f) / 3.0f
            if (f > 1.0f) {
                f = 1.0f
            }

            return f
        }
    }
}
