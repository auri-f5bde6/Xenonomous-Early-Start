package com.github.auri_f5bde6.xeno_early_start.item

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStartConfig
import com.github.auri_f5bde6.xeno_early_start.block.PrimitiveFireBlock
import com.github.auri_f5bde6.xeno_early_start.block.block_entity.PrimitiveFireBlockEntity
import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartBlockRegistry
import net.minecraft.block.Block
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.registry.tag.BlockTags
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.UseAction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import net.minecraft.world.event.GameEvent
import net.minecraftforge.common.Tags
import java.util.function.Consumer

class FireStarterItem(settings: Settings) : net.minecraft.item.Item(settings) {
    override fun getUseAction(stack: ItemStack?): UseAction {
        return UseAction.BOW
    }

    private fun putBlockPos(stack: ItemStack, pos: BlockPos) {
        val tag = stack.getOrCreateNbt()
        tag.putInt("pos_x", pos.x)
        tag.putInt("pos_y", pos.y)
        tag.putInt("pos_z", pos.z)
    }

    private fun getBlockPos(stack: ItemStack): BlockPos {
        val tag = stack.getOrCreateNbt()
        return BlockPos(tag.getInt("pos_x"), tag.getInt("pos_y"), tag.getInt("pos_z"))
    }

    override fun finishUsing(stack: ItemStack, world: World, user: LivingEntity): ItemStack {

        val aboveBlock = getBlockPos(stack)
        var newStack = stack.copy()
        newStack.damage(
            1, user
        ) { e: LivingEntity -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND) }
        var chance = 0.25
        if (world.hasRain(aboveBlock)) {
            chance = 0.0
        } else if (world.getBiome(aboveBlock).isIn(Tags.Biomes.IS_DRY)) {
            chance = 0.5
        }
        if (chance > world.random.nextFloat()) {
            val box = Box(aboveBlock, aboveBlock.add(1, 1, 1))
            val items = world.getEntitiesByClass(ItemEntity::class.java, box) { true }
            var burnTime = 0
            for (item in items) {
                burnTime = PrimitiveFireBlock.maybeConsumeStack(item.stack, 0, false)
            }
            // At least 30 second worth of burn time required
            if (burnTime >= 600) {

                // I mean it works, and the alternative is O(n) still so /shrug
                for (item in items) {
                    var x = 0
                    x = PrimitiveFireBlock.maybeConsumeStack(item.stack, x, true)
                    if (x > XenoEarlyStartConfig.config.earlyGameChanges.primitiveFire.maxBurnTime) {
                        break
                    }
                }

                if (world.getBlockState(aboveBlock).isAir || world.getBlockState(aboveBlock)
                        .isIn(BlockTags.REPLACEABLE_BY_TREES)
                ) {
                    if (!(user is PlayerEntity && user.isCreative)) {
                        newStack = ItemStack.EMPTY
                    }
                    world.setBlockState(
                        aboveBlock,
                        XenoEarlyStartBlockRegistry.PRIMITIVE_FIRE.get().defaultState.with(
                            PrimitiveFireBlock.FACING, user.horizontalFacing
                        )
                    )
                    (world.getBlockEntity(aboveBlock) as PrimitiveFireBlockEntity).burnTime = burnTime
                }
            }
        }
        if (user is PlayerEntity) {
            user.addExhaustion(36f)
        }
        return newStack
    }

    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        val world = context.world
        val blockPos = context.blockPos
        val blockState = context.world.getBlockState(context.blockPos)
        if (PrimitiveFireBlock.canBeLit(blockState)) {
            val player = context.player
            world.playSound(
                player,
                blockPos,
                SoundEvents.ITEM_FLINTANDSTEEL_USE,
                SoundCategory.BLOCKS,
                1.0f,
                world.getRandom().nextFloat() * 0.4f + 0.8f
            )
            world.setBlockState(
                blockPos,
                blockState.with(Properties.LIT, true),
                Block.NOTIFY_ALL or Block.REDRAW_ON_MAIN_THREAD
            )
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, blockPos)
            if (player != null) {
                context.stack.damage(
                    Int.MAX_VALUE,
                    player,
                    Consumer { p: PlayerEntity -> p.sendToolBreakStatus(context.hand) })
            }

            return ActionResult.success(world.isClient())
        } else {
            if (context.world.isClient) {
                PrimitiveFireBlock.spawnSmokeParticle(context.world, context.blockPos, false)
            }
            if (context.side == Direction.UP) {
                val stack = context.stack
                val aboveBlock = context.blockPos.add(0, 1, 0)
                putBlockPos(stack, aboveBlock)
                context.player?.setCurrentHand(context.hand)
                return ActionResult.CONSUME
            }
            return ActionResult.FAIL
        }


    }

    override fun usageTick(world: World, user: LivingEntity?, stack: ItemStack, remainingUseTicks: Int) {
        super.usageTick(world, user, stack, remainingUseTicks)
        if (world.isClient && world.random.nextFloat() < 0.15) {
            PrimitiveFireBlock.spawnSmokeParticle(world, getBlockPos(stack), true)
        }
    }

    override fun getMaxUseTime(stack: ItemStack?): Int {
        return 60
    }
}