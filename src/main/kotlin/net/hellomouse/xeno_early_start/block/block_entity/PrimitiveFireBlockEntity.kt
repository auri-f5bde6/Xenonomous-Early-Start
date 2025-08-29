package net.hellomouse.xeno_early_start.block.block_entity

import net.hellomouse.xeno_early_start.registries.ProgressionModBlockEntityRegistry
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.Clearable
import net.minecraft.util.math.BlockPos

class PrimitiveFireBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(
    ProgressionModBlockEntityRegistry.PRIMITIVE_FIRE.get(), pos, state
), Clearable {
    override fun clear() {
        TODO("Not yet implemented")
    }
}