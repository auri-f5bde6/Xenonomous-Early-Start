package com.github.auri_f5bde6.xeno_early_start.block

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStartConfig
import com.github.auri_f5bde6.xeno_early_start.advancements.PrimitiveFireCriterion
import com.github.auri_f5bde6.xeno_early_start.advancements.XenoEarlyStartCriteria
import com.github.auri_f5bde6.xeno_early_start.block.block_entity.PrimitiveFireBlockEntity
import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartBlockEntityRegistry
import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartBlockRegistry
import com.github.auri_f5bde6.xeno_early_start.utils.TransUtils
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.particle.ParticleTypes
import net.minecraft.recipe.RecipeType
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.stat.Stats
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.IntProperty
import net.minecraft.state.property.Properties
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraft.world.event.GameEvent
import net.minecraftforge.common.ForgeHooks.getBurnTime
import kotlin.math.min

// Some code borrowed form vanilla campfire
class PrimitiveFireBlock : BlockWithEntity, Waterloggable {


    var fireDamage: Int

    constructor(fireDamage: Int, settings: Settings) : super(settings) {
        this.fireDamage = fireDamage
        this.defaultState = this.stateManager.getDefaultState()
            .with(LIT, true)
            .with(FACING, Direction.NORTH)
            .with(WATERLOGGED, false)
    }

    companion object {
        @JvmField
        val LIT: BooleanProperty = Properties.LIT
        val LIGHT_LEVEL: IntProperty = IntProperty.of("light_level", 0, 15)
        val FACING: DirectionProperty = Properties.HORIZONTAL_FACING
        val WATERLOGGED: BooleanProperty = Properties.WATERLOGGED
        val SHAPE: VoxelShape = VoxelShapes.cuboid(0.125, 0.0, 0.125, 0.875, 0.3125, 0.8125)
        val SHAPE_ROTATED: VoxelShape = TransUtils.rotateY(SHAPE)
        fun createLightLevel(): (BlockState) -> Int {
            return { blockState: BlockState ->
                if (blockState.get(LIT)) {
                    blockState.get(LIGHT_LEVEL)
                } else {
                    0
                }
            }
        }

        fun spawnSmokeParticle(world: World, pos: BlockPos, lotsOfSmoke: Boolean) {
            val randomSource = world.getRandom()
            val simpleParticleType = ParticleTypes.CAMPFIRE_COSY_SMOKE
            world.addImportantParticle(
                simpleParticleType,
                true,
                pos.x + 0.2 + randomSource.nextDouble() / 3.0 * (if (randomSource.nextBoolean()) 1 else -1),
                pos.y - 0.05 + randomSource.nextDouble() + randomSource.nextDouble(),
                pos.z + 0.2 + randomSource.nextDouble() / 3.0 * (if (randomSource.nextBoolean()) 1 else -1),
                0.0,
                0.07,
                0.0
            )
            world.addParticle(ParticleTypes.FLAME, pos.x + 0.5, pos.y + 0.34, pos.z + 0.5, 0.0, 0.0, 0.0)
            if (lotsOfSmoke) {
                world.addParticle(
                    ParticleTypes.SMOKE,
                    pos.x + 0.2 + randomSource.nextDouble() / 4.0 * (if (randomSource.nextBoolean()) 1 else -1),
                    pos.y + 0.4,
                    pos.z + 0.2 + randomSource.nextDouble() / 4.0 * (if (randomSource.nextBoolean()) 1 else -1),
                    0.0,
                    0.005,
                    0.0
                )
            }
        }

        fun extinguish(entity: Entity?, world: WorldAccess, pos: BlockPos, state: BlockState) {
            if (world.isClient) {
                for (i in 0..19) {
                    spawnSmokeParticle(
                        world as World,
                        pos,
                        true
                    )
                }
            }
            val blockEntity = world.getBlockEntity(pos)
            if (blockEntity is PrimitiveFireBlockEntity) {
                blockEntity.spawnItemsBeingCooked()
            }
            world.emitGameEvent(entity, GameEvent.BLOCK_CHANGE, pos)
        }

        @JvmStatic
        fun canBeLit(state: BlockState, context: ItemUsageContext, refuel: Int? = null): Boolean {
            if (!(state.isOf(XenoEarlyStartBlockRegistry.PRIMITIVE_FIRE.get())
                        && !state.get(WATERLOGGED) && !state.get(LIT))
            ) {
                return false
            }
            val blockEntity = context.world.getBlockEntity(context.blockPos)
            if (blockEntity is PrimitiveFireBlockEntity) {
                if (refuel != null) {
                    blockEntity.burnTime = blockEntity.burnTime + refuel
                }
                if (blockEntity.burnTime > 0) {
                    return true
                }
                context.player?.sendMessage(Text.translatable("xeno_early_start.gameplay.not_enough_fuel"), true)
            }
            return false
        }

        @JvmStatic
        fun canBeLit(state: BlockState, context: ItemUsageContext): Boolean {
            return canBeLit(state, context, refuel = null)
        }

        fun maybeConsumeStack(stack: ItemStack, oldFuelTime: Int, consume: Boolean): Int {
            val maximumBurnTime = XenoEarlyStartConfig.config.earlyGameChanges.primitiveFire.maxBurnTime
            val multi = XenoEarlyStartConfig.config.earlyGameChanges.primitiveFire.fuelTimeMultiplier
            val individual = (getBurnTime(ItemStack(stack.item, 1), RecipeType.SMELTING) * multi).toInt()
            if (individual != 0) {
                val toConsume = min(((maximumBurnTime - oldFuelTime) / individual), stack.count)
                if (toConsume > 0) {
                    if (consume) {
                        stack.decrement(toConsume)
                    }
                    return oldFuelTime + individual * toConsume
                }
            }
            return oldFuelTime
        }

        fun maybeConsumeStack(stack: ItemStack, oldFuelTime: Int): Int {
            return maybeConsumeStack(stack, oldFuelTime, true)
        }
    }

    override fun createBlockEntity(
        pos: BlockPos, state: BlockState
    ): BlockEntity {
        return PrimitiveFireBlockEntity(pos, state)
    }

    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState, world: BlockView?, pos: BlockPos?, context: ShapeContext?
    ): VoxelShape? {
        val dir = state.get(FACING)
        return when (dir) {
            Direction.DOWN, Direction.UP -> throw Exception("HORIZONTAL_FACING direction should not contain UP or down")
            Direction.NORTH, Direction.SOUTH -> SHAPE
            Direction.WEST, Direction.EAST -> SHAPE_ROTATED
        }
    }

    override fun onSteppedOn(world: World, pos: BlockPos, state: BlockState, entity: Entity) {
        if (state.get(LIT) && entity is LivingEntity && !EnchantmentHelper.hasFrostWalker(entity)) {
            entity.setOnFireFor(1)
            entity.damage(world.damageSources.inFire(), this.fireDamage.toFloat())
        }
        super.onSteppedOn(world, pos, state, entity)
    }


    override fun getRenderType(state: BlockState?): BlockRenderType {
        return BlockRenderType.MODEL
    }

    @Deprecated("Deprecated in Java")
    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState {
        return state.with(
            FACING,
            rotation.rotate(state.get(FACING))
        )
    }

    @Deprecated("Deprecated in Java")
    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState {
        @Suppress("DEPRECATION")
        return state.rotate(mirror.getRotation(state.get(FACING)))
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(LIT, FACING, WATERLOGGED, LIGHT_LEVEL)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        val levelAccessor: WorldAccess = ctx.world
        val blockPos = ctx.blockPos
        val flag = levelAccessor.getFluidState(blockPos).fluid === Fluids.WATER
        return this.defaultState
            .with(FACING, ctx.horizontalPlayerFacing)
            .with(WATERLOGGED, flag)
    }

    @Deprecated("Deprecated in Java")
    override fun getFluidState(state: BlockState): FluidState? {
        @Suppress("Deprecation")
        return if (state.get(SlabBlock.WATERLOGGED)) Fluids.WATER.getStill(false) else super.getFluidState(
            state
        )
    }

    override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: Random) {
        if (state.get(LIT)) {
            if (random.nextInt(10) == 0) {
                world.playSound(
                    pos.x + 0.5,
                    pos.y + 0.5,
                    pos.z + 0.5,
                    SoundEvents.BLOCK_CAMPFIRE_CRACKLE,
                    SoundCategory.BLOCKS,
                    0.5f + random.nextFloat(),
                    random.nextFloat() * 0.7f + 0.6f,
                    false
                )
            }

            if (random.nextInt(5) == 0) {
                for (i in 0..<random.nextInt(1) + 1) {
                    world.addParticle(
                        ParticleTypes.LAVA,
                        pos.x + 0.5,
                        pos.y + 0.5,
                        pos.z + 0.5,
                        (random.nextFloat() / 2.0f).toDouble(),
                        5.0E-5,
                        (random.nextFloat() / 2.0f).toDouble()
                    )
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onEntityCollision(state: BlockState, world: World, pos: BlockPos, entity: Entity) {
        if (entity is ItemEntity && entity.owner != PrimitiveFireBlockEntity.ITEM_UUID) {
            val primitiveFire = (world.getBlockEntity(pos)) as PrimitiveFireBlockEntity
            val oldTime = primitiveFire.burnTime
            primitiveFire.burnTime = maybeConsumeStack(entity.stack, oldTime)
            val owner = entity.owner
            if (oldTime != primitiveFire.burnTime && !world.isClient && owner is ServerPlayerEntity) {
                XenoEarlyStartCriteria.PRIMITIVE_FIRE_CREATION.trigger(
                    owner,
                    PrimitiveFireCriterion.Conditions.Type.REFUEL
                )
            }
        }
    }

    override fun tryFillWithFluid(
        world: WorldAccess,
        pos: BlockPos,
        state: BlockState,
        fluidState: FluidState
    ): Boolean {
        if (!state.get(Properties.WATERLOGGED) && fluidState.fluid === Fluids.WATER) {
            val flag = state.get(LIT) as Boolean
            if (flag) {
                if (!world.isClient) {
                    world.playSound(
                        null as PlayerEntity?,
                        pos,
                        SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE,
                        SoundCategory.BLOCKS,
                        1.0f,
                        1.0f
                    )
                }

                extinguish(null as Entity?, world, pos, state)
            }

            world.setBlockState(
                pos,
                state.with(WATERLOGGED, true)
                    .with(LIT, false),
                NOTIFY_ALL
            )
            world.scheduleFluidTick(pos, fluidState.fluid, fluidState.fluid.getTickRate(world))
            return true
        } else {
            return false
        }
    }

    override fun <T : BlockEntity> getTicker(
        world: World,
        state: BlockState,
        type: BlockEntityType<T>
    ): BlockEntityTicker<T>? {
        if (world.isClient) {
            return if (state.get(LIT)) checkType(
                type,
                XenoEarlyStartBlockEntityRegistry.PRIMITIVE_FIRE.get(),
                BlockEntityTicker { world: World, pos: BlockPos, state: BlockState, campfire: PrimitiveFireBlockEntity ->
                    PrimitiveFireBlockEntity.clientTick(
                        world,
                        pos,
                        state,
                        campfire
                    )
                }) else null
        } else {
            return if (state.get(LIT))
                checkType(
                    type,
                    XenoEarlyStartBlockEntityRegistry.PRIMITIVE_FIRE.get(),
                    BlockEntityTicker { world: World, pos: BlockPos, state: BlockState, campfire: PrimitiveFireBlockEntity ->
                        PrimitiveFireBlockEntity.litServerTick(
                            world,
                            pos,
                            state,
                            campfire
                        )
                    })
            else
                checkType(
                    type,
                    XenoEarlyStartBlockEntityRegistry.PRIMITIVE_FIRE.get(),
                    BlockEntityTicker { world: World, pos: BlockPos, state: BlockState, campfire: PrimitiveFireBlockEntity ->
                        PrimitiveFireBlockEntity.unlitServerTick(
                            world,
                            pos,
                            state,
                            campfire
                        )
                    })
        }
    }

    @Deprecated("Deprecated in Java")
    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: WorldAccess,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world))
        }
        @Suppress("Deprecation")
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hit: BlockHitResult
    ): ActionResult {
        val blockEntity = world.getBlockEntity(pos)
        if (blockEntity is PrimitiveFireBlockEntity) {
            val itemStack = player.getStackInHand(hand)
            val optional = blockEntity.getRecipeFor(itemStack)
            if (optional.isPresent) {
                if (!world.isClient
                    && blockEntity.addItem(
                        player,
                        if (player.abilities.creativeMode) itemStack.copy() else itemStack,
                        optional.get().getCookTime() * 2
                    )
                ) {
                    player.incrementStat(Stats.INTERACT_WITH_CAMPFIRE)
                    if (player is ServerPlayerEntity) {
                        XenoEarlyStartCriteria.PRIMITIVE_FIRE_CREATION.trigger(
                            player,
                            PrimitiveFireCriterion.Conditions.Type.USE, itemStack
                        )
                    }
                    return ActionResult.SUCCESS
                }

                return ActionResult.CONSUME
            }
        }

        return ActionResult.PASS
    }

}