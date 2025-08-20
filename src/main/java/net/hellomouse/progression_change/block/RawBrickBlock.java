package net.hellomouse.progression_change.block;

import net.hellomouse.progression_change.registries.ProgressionModBlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public class RawBrickBlock extends BrickBlock {
    public RawBrickBlock(Settings arg) {
        super(arg);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.isAreaLoaded(pos, 1)) {
            if (random.nextInt(13) == 0) {
                world.removeBlock(pos, false);
                world.setBlockState(pos,
                        ProgressionModBlockRegistry.BRICK.get().getDefaultState()
                                .with(FACING, state.get(FACING))
                                .with(VERTICAL, state.get(VERTICAL))
                );
            }
        }
    }
}
