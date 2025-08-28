package net.hellomouse.xeno_early_start.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.HorizontalFacingBlock
import net.minecraft.block.ShapeContext
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView

open class BrickBlock(arg: Settings) : HorizontalFacingBlock(arg) {
    init {
        this.defaultState = this.stateManager.getDefaultState().with<Direction?, Direction?>(FACING, Direction.NORTH)
        this.defaultState = this.stateManager.getDefaultState().with<Boolean?, Boolean?>(VERTICAL, false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block?, BlockState?>) {
        builder.add(FACING)
        builder.add(VERTICAL)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        val facing = ctx.horizontalPlayerFacing.opposite
        val vertical = ctx.side == Direction.UP || ctx.side == Direction.DOWN

        return this.defaultState
            .with<Direction?, Direction?>(FACING, facing)
            .with<Boolean?, Boolean?>(VERTICAL, vertical)
    }

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape? {
        if (state.get<Boolean?>(VERTICAL)) {
            if (state.get<Direction?>(FACING) == Direction.NORTH || state.get<Direction?>(FACING) == Direction.SOUTH) {
                return SHAPE_UP
            } else {
                return SHAPE_UP_ROTATED
            }
        } else {
            if (state.get<Direction?>(FACING) == Direction.NORTH || state.get<Direction?>(FACING) == Direction.SOUTH) {
                return SHAPE
            } else {
                return SHAPE_ROTATED
            }
        }
    }

    override fun getCullingShape(state: BlockState?, world: BlockView?, pos: BlockPos?): VoxelShape? {
        return VoxelShapes.empty()
    }

    companion object {
        val VERTICAL: BooleanProperty = BooleanProperty.of("vertical")
        private val SHAPE: VoxelShape? = VoxelShapes.cuboid(0.375, 0.0, 0.28125, 0.625, 0.1875, 0.71875)
        private val SHAPE_ROTATED: VoxelShape? = VoxelShapes.cuboid(0.28125, 0.0, 0.375, 0.71875, 0.1875, 0.625)

        private val SHAPE_UP: VoxelShape? = VoxelShapes.cuboid(0.375, 0.0, 0.40625, 0.625, 0.4375, 0.59375)
        private val SHAPE_UP_ROTATED: VoxelShape? = VoxelShapes.cuboid(0.40625, 0.0, 0.375, 0.59375, 0.4375, 0.625)
    }
}
