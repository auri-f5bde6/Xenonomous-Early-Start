package net.hellomouse.progression_change.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class BrickBlock extends HorizontalFacingBlock {
    public static final BooleanProperty VERTICAL = BooleanProperty.of("vertical");
    private static final VoxelShape SHAPE = VoxelShapes.cuboid(0.375, 0, 0.28125, 0.625, 0.1875, 0.71875);
    private static final VoxelShape SHAPE_ROTATED = VoxelShapes.cuboid(0.28125, 0, 0.375, 0.71875, 0.1875, 0.625);

    private static final VoxelShape SHAPE_UP = VoxelShapes.cuboid(0.375, 0, 0.40625, 0.625, 0.4375, 0.59375);
    private static final VoxelShape SHAPE_UP_ROTATED = VoxelShapes.cuboid(0.40625, 0, 0.375, 0.59375, 0.4375, 0.625);

    protected BrickBlock(Settings arg) {
        super(arg);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
        this.setDefaultState(this.stateManager.getDefaultState().with(VERTICAL, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<net.minecraft.block.Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(VERTICAL);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction facing = ctx.getHorizontalPlayerFacing().getOpposite();
        boolean vertical = ctx.getSide() == Direction.UP || ctx.getSide() == Direction.DOWN;

        return this.getDefaultState()
                .with(FACING, facing)
                .with(VERTICAL, vertical);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(VERTICAL)) {
            if (state.get(FACING) == Direction.NORTH || state.get(FACING) == Direction.SOUTH) {
                return SHAPE_UP;
            } else {
                return SHAPE_UP_ROTATED;
            }
        } else {
            if (state.get(FACING) == Direction.NORTH || state.get(FACING) == Direction.SOUTH) {
                return SHAPE;
            } else {
                return SHAPE_ROTATED;
            }
        }
    }

    @Override
    public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
        return VoxelShapes.empty();
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.isAreaLoaded(pos, 1)) {
            if (random.nextInt(13) == 0) {
                world.removeBlock(pos, false);
                world.setBlockState(pos, Blocks.DIAMOND_BLOCK.getDefaultState());
            }
        }
    }
}
