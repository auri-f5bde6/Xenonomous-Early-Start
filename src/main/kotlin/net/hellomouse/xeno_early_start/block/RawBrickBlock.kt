package net.hellomouse.xeno_early_start.block

import net.hellomouse.xeno_early_start.registries.ProgressionModBlockRegistry
import net.minecraft.block.BlockState
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random

class RawBrickBlock(arg: Settings) : BrickBlock(arg) {
    @Deprecated("Deprecated in Java, I guess")
    override fun randomTick(state: BlockState, world: ServerWorld, pos: BlockPos?, random: Random) {
        @Suppress("DEPRECATION")
        if (world.isAreaLoaded(pos, 1)) {
            if (random.nextInt(13) == 0) {
                world.removeBlock(pos, false)
                world.setBlockState(
                    pos,
                    ProgressionModBlockRegistry.BRICK.get().defaultState
                        .with(FACING, state.get(FACING))
                        .with(
                            VERTICAL,
                            state.get(VERTICAL)
                        )
                )
            }
        }
    }
}
