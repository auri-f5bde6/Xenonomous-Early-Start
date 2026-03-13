package com.github.auri_f5bde6.xeno_early_start.block

import com.github.auri_f5bde6.xeno_early_start.config.XenoEarlyStartConfig
import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartBlockRegistry
import com.github.auri_f5bde6.xeno_early_start.utils.OtherUtils
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
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
import net.minecraft.world.Heightmap
import net.minecraft.world.World
import net.minecraftforge.common.Tags
import net.minecraftforge.registries.ForgeRegistries
import kotlin.math.max
import kotlin.math.min

class RawBrickBlock(arg: Settings) : BrickBlock(arg) {
    init {
        this.defaultState = this.stateManager.getDefaultState().with(AXIS, Direction.Axis.X).with(DRYING_LEVEL, 0)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(DRYING_LEVEL)
    }

    override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, rand: Random) {
        val p = getDryProbability(world, pos)
        if (p != null && p > 0) {
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
            if (rand.nextFloat() > 0.5) {
                world.playSoundAtBlockCenter(
                    pos,
                    SoundEvents.BLOCK_FIRE_EXTINGUISH,
                    SoundCategory.BLOCKS,
                    0.05f + rand.nextFloat() * 0.05f,
                    rand.nextFloat() * 0.7f + 0.6f,
                    true
                )
            }
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

    private fun isRaining(world: World, pos: BlockPos): Boolean {
        return (world.isRaining || world.isThundering) && !(world.biomeAccess.getBiome(pos).isIn(Tags.Biomes.IS_DRY))
    }

    /// Return null when the drying stage should be reset back to 0
    private fun getDryProbability(world: World, pos: BlockPos): Float? {
        val top = world.getBlockState(world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, pos).add(0, -1, 0))
        val bottom = world.getBlockState(world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, pos))
        var coveredByGlass = false
        coveredByGlass =
            OtherUtils.raycast(
                world,
                pos,
                Direction.UP,
                world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, pos).subtract(pos).y + 1
            ) { b ->
                coveredByGlass = true
                b.isIn(
                    Tags.Blocks.GLASS
                )
            } == null && coveredByGlass
        val canSeeSky =
            (world.isSkyVisible(pos) && (top.isIn(Tags.Blocks.GLASS) || bottom.isOf(XenoEarlyStartBlockRegistry.RAW_BRICK.get())))
        val raining = isRaining(world, pos)

        world.calculateAmbientDarkness()
        if (raining && !coveredByGlass) {
            return null
        }
        if (world.timeOfDay % 24000 < 12000 && canSeeSky) {
            return if (!raining) {
                // With no glass covering
                1f
            } else {
                // With glass covering
                0.76f
            }
        }
        return 0f
    }

    private fun dryTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random, probability: Float) {
        var newLevel = state[DRYING_LEVEL]
        val finishDryingAt = XenoEarlyStartConfig.config.earlyGameChanges.rawBrickDryingLength
        if (random.nextFloat() < probability && state[DRYING_LEVEL] < finishDryingAt) {
            var x = 1
            for (dx in -3..3) {
                for (dz in -3..3) {
                    for (dy in -2..2) {
                        val blockState = world.getBlockState(pos.add(dx, dy, dz))
                        x = max(
                            if (blockState.isOf(Blocks.LAVA) || blockState.isOf(Blocks.LAVA_CAULDRON)) {
                                14
                            } else if (blockState.isOf(Blocks.CAMPFIRE)) {
                                5
                            } else if (blockState.isOf(Blocks.FIRE)) {
                                4
                            } else if (blockState.isOf(XenoEarlyStartBlockRegistry.PRIMITIVE_FIRE.get()) && blockState.get(
                                    PrimitiveFireBlock.LIT
                                ) || blockState.isOf(Blocks.TORCH)
                            ) {
                                3
                            } else {
                                1
                            }, x
                        )
                    }
                }
            }
            newLevel = min(MAX_DRY_LEVEL, state[DRYING_LEVEL] + x)
            world.setBlockState(pos, state.with(DRYING_LEVEL, newLevel))
        }
        if (newLevel >= finishDryingAt) {
            world.playSound(
                null,
                pos.x + 0.5,
                pos.y + 0.5,
                pos.z + 0.5,
                ForgeRegistries.SOUND_EVENTS.getHolder(SoundEvents.BLOCK_DEEPSLATE_BRICKS_PLACE).get(),
                SoundCategory.BLOCKS,
                0.95f + random.nextFloat() * 0.05f,
                random.nextFloat() * 0.7f + 0.6f,
                world.random.nextLong()
            )
            world.removeBlock(pos, false)
            world.setBlockState(
                pos,
                XenoEarlyStartBlockRegistry.BRICK.get().defaultState
                    .with(AXIS, state[AXIS])
            )

        }
    }

    override fun onSteppedOn(world: World, pos: BlockPos, state: BlockState, entity: Entity) {
        super.onSteppedOn(world, pos, state, entity)
        if (!entity.isSneaking && entity !is ItemEntity) {
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
        const val MAX_DRY_LEVEL = 20
        val DRYING_LEVEL: IntProperty = IntProperty.of("drying_level", 0, MAX_DRY_LEVEL)
    }
}
