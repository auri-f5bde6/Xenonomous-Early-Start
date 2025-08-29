package net.hellomouse.xeno_early_start.block

import net.hellomouse.xeno_early_start.block.block_entity.PrimitiveFireBlockEntity
import net.hellomouse.xeno_early_start.utils.TransUtils
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World


class PrimitiveFIreBlock : BlockWithEntity {
    var fireDamage: Int

    constructor(fireDamage: Int, settings: Settings) : super(settings) {
        this.fireDamage = fireDamage
        this.defaultState = this.stateManager.getDefaultState().with<Boolean, Boolean>(LIT, false)
            .with(FACING, Direction.NORTH)
    }

    companion object {
        val LIT: BooleanProperty = Properties.LIT
        val FACING: DirectionProperty = Properties.HORIZONTAL_FACING

        val SHAPE: VoxelShape = VoxelShapes.cuboid(0.125, 0.0, 0.125, 0.875, 0.3125, 0.8125)
        val SHAPE_ROTATED: VoxelShape = TransUtils.rotateY(SHAPE)
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

    @Deprecated("Deprecated in Java")
    override fun onEntityCollision(state: BlockState, world: World, pos: BlockPos?, entity: Entity?) {
        if (state.get(LIT) && entity is LivingEntity && !EnchantmentHelper.hasFrostWalker(entity)) {
            entity.damage(world.damageSources.inFire(), this.fireDamage.toFloat())
        }
        @Suppress("DEPRECATION")
        super.onEntityCollision(state, world, pos, entity)
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
        builder.add(LIT, FACING)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        return this.defaultState
            .with(FACING, ctx.horizontalPlayerFacing)
    }
}