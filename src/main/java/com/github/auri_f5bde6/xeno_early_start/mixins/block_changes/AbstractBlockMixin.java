package com.github.auri_f5bde6.xeno_early_start.mixins.block_changes;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin {
    @WrapMethod(method = "onEntityCollision")
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, Operation<Void> original) {
        original.call(state, world, pos, entity);
    }

    @WrapMethod(method = "scheduledTick")
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random, Operation<Void> original) {
        original.call(state, world, pos, random);
    }

    @WrapMethod(method = "onBlockAdded")
    @Deprecated
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify, Operation<Void> original) {
        original.call(state, world, pos, oldState, notify);
    }
}
