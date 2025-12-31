package com.github.auri_f5bde6.xeno_early_start.mixins.block_changes;

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStartConfig;
import com.github.auri_f5bde6.xeno_early_start.block.BrickFurnaceBlock;
import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartBlockRegistry;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import static com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartDamageSource.*;

@Mixin(Block.class)
public class BlockMixin {
    @WrapMethod(method = "onSteppedOn")
    private void damagedByCertainBlocks(World world, BlockPos pos, BlockState state, Entity entity, Operation<Void> original) {
        if (state.isOf(Blocks.STONECUTTER)) {
            entity.damage(getDamageSource(STONECUTTER, world.getRegistryManager()), XenoEarlyStartConfig.config.blockChanges.getStonecutterDamage());
        } else if (state.isOf(Blocks.SMOKER) && state.get(SmokerBlock.LIT)) {
            entity.damage(getDamageSource(FURNACE, world.getRegistryManager()), 0.5f);
        } else if (state.isOf(XenoEarlyStartBlockRegistry.BRICK_FURNACE.get()) && state.get(BrickFurnaceBlock.LIT)) {
            entity.damage(getDamageSource(FURNACE, world.getRegistryManager()), 1f);
        } else if (state.isOf(Blocks.FURNACE) && state.get(FurnaceBlock.LIT)) {
            entity.damage(getDamageSource(FURNACE, world.getRegistryManager()), 1.5f);
        } else if (state.isOf(Blocks.BLAST_FURNACE) && state.get(BlastFurnaceBlock.LIT)) {
            entity.damage(getDamageSource(FURNACE, world.getRegistryManager()), 2f);
            entity.setOnFireFor(3);
        }
        original.call(world, pos, state, entity);

    }


    @WrapMethod(method = "onLandedUpon")
    private void pointyAmethyst(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance, Operation<Void> original) {
        if (state.isOf(Blocks.AMETHYST_CLUSTER) && state.get(AmethystClusterBlock.FACING) == Direction.UP) {
            entity.handleFallDamage(fallDistance + 2.0F, XenoEarlyStartConfig.config.blockChanges.getAmethystFallDamageMultiplier(), getDamageSource(AMETHYST, world.getRegistryManager()));
        }
        original.call(world, state, pos, entity, fallDistance);
    }
}
