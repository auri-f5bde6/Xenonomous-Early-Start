package net.hellomouse.xeno_early_start.item

import net.hellomouse.xeno_early_start.block.PrimitiveFireBlock
import net.hellomouse.xeno_early_start.registries.ProgressionModBlockRegistry
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.recipe.RecipeType
import net.minecraft.util.ActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class FireStarterItem(settings: Settings) : Item(settings) {
    private fun putBlockPos(stack: ItemStack, pos: BlockPos) {
        val tag=stack.getOrCreateNbt()
        tag.putInt("pos_x", pos.x)
        tag.putInt("pos_y", pos.y)
        tag.putInt("pos_z", pos.z)
    }
    private fun getBlockPos(stack: ItemStack): BlockPos {
        val tag=stack.getOrCreateNbt()
        return BlockPos(tag.getInt("pos_x"), tag.getInt("pos_y"), tag.getInt("pos_z"))
    }

    override fun finishUsing(stack: ItemStack, world: World, user: LivingEntity): ItemStack {
        stack.damage(
            1, user
        ) { e: LivingEntity -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND) }
        if (!(0.25 > world.random.nextFloat())) {
            val aboveBlock=getBlockPos(stack)
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
                    blockState = blockState.with(
                        PrimitiveFireBlock.FACING, user.horizontalFacing
                    )
                    world.setBlockState(aboveBlock, blockState)
                }
            }
        }
        return stack
    }
    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        if (context.side == Direction.UP) {
            val stack = context.stack
            val aboveBlock = context.blockPos.add(0, 1, 0)
            putBlockPos(stack, aboveBlock)
            context.player?.setCurrentHand(context.hand)
            return ActionResult.CONSUME
        }
        return ActionResult.FAIL

    }

    override fun getMaxUseTime(stack: ItemStack?): Int {
        return 60
    }
}