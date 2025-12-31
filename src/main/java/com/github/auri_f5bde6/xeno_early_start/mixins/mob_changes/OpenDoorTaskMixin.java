package com.github.auri_f5bde6.xeno_early_start.mixins.mob_changes;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.brain.task.OpenDoorsTask;
import net.minecraft.entity.mob.PiglinBruteEntity;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(OpenDoorsTask.class)
public abstract class OpenDoorTaskMixin {
    @WrapOperation(method = "pathToDoor", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/DoorBlock;setOpen(Lnet/minecraft/entity/Entity;Lnet/minecraft/world/World;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Z)V"))
    private static void breakDoor(
            DoorBlock instance, Entity entity, World world, BlockState state, BlockPos pos, boolean _open, Operation<Void> original
    ) {
        xeno_early_start$maybeBreakDoorOrOriginal(instance, entity, world, state, pos, _open, original);
    }

    @WrapOperation(method = "method_46966", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/DoorBlock;setOpen(Lnet/minecraft/entity/Entity;Lnet/minecraft/world/World;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Z)V"))
    private static void breakDoor2(
            DoorBlock instance, Entity entity, World world, BlockState state, BlockPos pos, boolean _open, Operation<Void> original
    ) {
        xeno_early_start$maybeBreakDoorOrOriginal(instance, entity, world, state, pos, _open, original);
    }

    @Unique
    private static void xeno_early_start$maybeBreakDoorOrOriginal(DoorBlock instance, Entity entity, World world, BlockState state, BlockPos pos, boolean _open, Operation<Void> original) {
        if (((entity instanceof PiglinEntity pe) && pe.getTarget() != null) || entity instanceof PiglinBruteEntity) {
            world.breakBlock(pos, true, entity);
        } else {
            original.call(instance, entity, world, state, pos, _open);
        }
    }
}
