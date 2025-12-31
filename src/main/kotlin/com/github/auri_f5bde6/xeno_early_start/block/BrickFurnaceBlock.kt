package com.github.auri_f5bde6.xeno_early_start.block

import com.github.auri_f5bde6.xeno_early_start.block.block_entity.BrickFurnaceBlockEntity
import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartBlockEntityRegistry
import net.minecraft.block.BlockState
import net.minecraft.block.FurnaceBlock
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.stat.Stats
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class BrickFurnaceBlock(arg: Settings) : FurnaceBlock(arg) {
    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return BrickFurnaceBlockEntity(pos, state)
    }

    override fun openScreen(world: World, pos: BlockPos, player: PlayerEntity) {
        val blockEntity = world.getBlockEntity(pos)
        if (blockEntity is BrickFurnaceBlockEntity) {
            player.openHandledScreen(blockEntity as NamedScreenHandlerFactory)
            player.incrementStat(Stats.INTERACT_WITH_FURNACE)
        }
    }

    override fun <T : BlockEntity?> getTicker(
        world: World,
        state: BlockState?,
        type: BlockEntityType<T?>?
    ): BlockEntityTicker<T?>? {
        return checkType<T?>(world, type, XenoEarlyStartBlockEntityRegistry.BRICK_FURNACE.get())
    }
}
