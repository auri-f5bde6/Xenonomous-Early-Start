package net.hellomouse.xeno_early_start.block

import net.hellomouse.xeno_early_start.ProgressionMod
import net.hellomouse.xeno_early_start.utils.TransUtils
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.EnumProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import kotlin.math.abs

open class BrickBlock(arg: Settings) : Block(arg) {
    init {
        this.defaultState = this.stateManager.getDefaultState().with(AXIS, Direction.Axis.X)
    }


    @Deprecated("Deprecated in Java, I guess")
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape {
        return when (state[AXIS]) {
            Direction.Axis.X -> SHAPE_ROTATED
            Direction.Axis.Y -> throw UnsupportedOperationException()
            Direction.Axis.Z -> SHAPE
        }

    }

    @Deprecated("Deprecated in Java, I guess")
    override fun getCullingShape(state: BlockState?, world: BlockView?, pos: BlockPos?): VoxelShape {
        return VoxelShapes.empty()
    }

    companion object {
        val AXIS: EnumProperty<Direction.Axis> = Properties.HORIZONTAL_AXIS
        private val SHAPE: VoxelShape = VoxelShapes.cuboid(0.375, 0.0, 0.28125, 0.625, 0.1875, 0.71875)
        private val SHAPE_ROTATED: VoxelShape = TransUtils.rotateY(SHAPE)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(AXIS)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        return this.defaultState.with(AXIS, ctx.horizontalPlayerFacing.axis)
    }
}
