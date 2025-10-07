package net.hellomouse.xeno_early_start.block

import net.hellomouse.xeno_early_start.registries.ProgressionModBlockRegistry
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.IntProperty
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import net.minecraft.world.Heightmap
import net.minecraft.world.World

class RawBrickBlock(arg: Settings) : BrickBlock(arg) {
    init {
        this.defaultState = this.stateManager.getDefaultState().with(AXIS, Direction.Axis.X).with(DRYING_LEVEL, 0)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(DRYING_LEVEL)
    }

    @Deprecated("Deprecated in Java, I guess")
    override fun randomTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
        @Suppress("DEPRECATION")
        if (world.isAreaLoaded(pos, 1) && canSeeSky(world, pos)) {
            if (world.isDay && !(world.isRaining || world.isThundering)) {
                if (random.nextFloat() < 0.95 && state[DRYING_LEVEL] < FINISH_DRYING_AT) {
                    world.setBlockState(pos, state.with(DRYING_LEVEL, state[DRYING_LEVEL] + 1))
                }
                if (state[DRYING_LEVEL] >= FINISH_DRYING_AT) {
                    world.removeBlock(pos, false)
                    world.setBlockState(
                        pos,
                        ProgressionModBlockRegistry.BRICK.get().defaultState
                            .with(AXIS, state[AXIS])
                    )
                }
            } else if (world.isRaining || world.isThundering) {
                world.setBlockState(pos, state.with(DRYING_LEVEL, 0))
            }
        }
    }

    companion object {
        const val FINISH_DRYING_AT=18
        val DRYING_LEVEL: IntProperty = IntProperty.of("dying_level", 0, FINISH_DRYING_AT)
        private fun canSeeSky(world: World, pos: BlockPos): Boolean {
            return world.getTopY(Heightmap.Type.WORLD_SURFACE, pos.x, pos.z)-1 <= pos.y
        }
    }
}
