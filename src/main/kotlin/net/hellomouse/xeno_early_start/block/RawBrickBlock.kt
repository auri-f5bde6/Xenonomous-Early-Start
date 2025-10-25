package net.hellomouse.xeno_early_start.block

import net.hellomouse.xeno_early_start.registries.ProgressionModBlockRegistry
import net.hellomouse.xeno_early_start.utils.OtherUtils.canSeeSky
import net.hellomouse.xeno_early_start.utils.OtherUtils.getBlockAbove
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.state.StateManager
import net.minecraft.state.property.IntProperty
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

class RawBrickBlock(arg: Settings) : BrickBlock(arg) {
    init {
        this.defaultState = this.stateManager.getDefaultState().with(AXIS, Direction.Axis.X).with(DRYING_LEVEL, 0)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(DRYING_LEVEL)
    }

    override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, rand: Random) {
        if (getDryProbability(world, pos) != null) {
            val particlePos = Vec3d.of(pos)
                .add(0.5 + (rand.nextFloat() - 0.5) * 0.2, rand.nextFloat() * 0.4, 0.5 + (rand.nextFloat() - 0.5) * 0.2)
            world.addParticle(
                ParticleTypes.POOF,
                particlePos.x,
                particlePos.y,
                particlePos.z,
                0.0,
                0.0,
                0.0
            )
            world.playSound(
                pos.x + 0.5,
                pos.y + 0.5,
                pos.z + 0.5,
                SoundEvents.BLOCK_FIRE_EXTINGUISH,
                SoundCategory.BLOCKS,
                0.1f + rand.nextFloat() * 0.1f,
                rand.nextFloat() * 0.7f + 0.6f,
                false
            )
        }
    }

    @Deprecated("Deprecated in Java, I guess")
    override fun randomTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
        @Suppress("DEPRECATION")
        if (world.isAreaLoaded(pos, 1)) {
            val probability = getDryProbability(world, pos)
            if (probability != null) {
                dryTick(state, world, pos, random, probability)
            } else {
                world.setBlockState(pos, state.with(DRYING_LEVEL, 0))
            }
        }
    }

    /// Return null when the drying stage should be reset back to 0
    private fun getDryProbability(world: World, pos: BlockPos): Float? {
        if (canSeeSky(world, pos)) {
            if (world.isDay && !(world.isRaining || world.isThundering)) {
                return 0.95f
            } else if ((world.isRaining || world.isThundering) && getBlockAbove(world, pos) != null) {
                return 0.76f
            }
        }
        return null
    }

    private fun dryTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random, probability: Float) {
        if (random.nextFloat() < probability && state[DRYING_LEVEL] < FINISH_DRYING_AT) {
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
    }

    override fun onSteppedOn(world: World, pos: BlockPos, state: BlockState, entity: Entity) {
        super.onSteppedOn(world, pos, state, entity)
        if (!entity.isSneaking) {
            world.breakBlock(pos, false)
            world.spawnEntity(
                ItemEntity(
                    world,
                    pos.x.toDouble(),
                    pos.y.toDouble(),
                    pos.z.toDouble(),
                    ItemStack(Items.CLAY_BALL, 2)
                )
            )
        }
    }

    companion object {
        const val FINISH_DRYING_AT = 18
        val DRYING_LEVEL: IntProperty = IntProperty.of("dying_level", 0, FINISH_DRYING_AT)
    }
}
