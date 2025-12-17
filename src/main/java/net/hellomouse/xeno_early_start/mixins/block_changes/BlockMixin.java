package net.hellomouse.xeno_early_start.mixins.block_changes;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.hellomouse.xeno_early_start.ProgressionModConfig;
import net.minecraft.block.AmethystClusterBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import static net.hellomouse.xeno_early_start.registries.ProgressionModDamageSource.*;

@Mixin(Block.class)
public class BlockMixin {
    @WrapMethod(method = "onSteppedOn")
    private void damagedByCertainBlocks(World world, BlockPos pos, BlockState state, Entity entity, Operation<Void> original) {
        if (state.isOf(Blocks.STONECUTTER)) {
            entity.damage(getDamageSource(STONECUTTER, world.getRegistryManager()), ProgressionModConfig.config.blockChanges.getStonecutterDamage());
        }
        original.call(world, pos, state, entity);

    }


    @WrapMethod(method = "onLandedUpon")
    private void pointyAmethyst(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance, Operation<Void> original) {
        if (state.isOf(Blocks.AMETHYST_CLUSTER) && state.get(AmethystClusterBlock.FACING) == Direction.UP) {
            entity.handleFallDamage(fallDistance + 2.0F, ProgressionModConfig.config.blockChanges.getAmethystFallDamageMultiplier(), getDamageSource(AMETHYST, world.getRegistryManager()));
        }
        original.call(world, state, pos, entity, fallDistance);
    }
}
