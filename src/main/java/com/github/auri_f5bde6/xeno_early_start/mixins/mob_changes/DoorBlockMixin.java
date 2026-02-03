package com.github.auri_f5bde6.xeno_early_start.mixins.mob_changes;

import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.PiglinBruteEntity;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DoorBlock.class)
public class DoorBlockMixin {
    @Inject(method = "setOpen", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", shift = At.Shift.BEFORE))
    void beakDoor(Entity entity, World world, BlockState state, BlockPos pos, boolean _open, CallbackInfo ci) {
        if (((entity instanceof PiglinEntity pe) && pe.getTarget() != null) || entity instanceof PiglinBruteEntity) {
            world.breakBlock(pos, true, entity);
        }
    }
}
