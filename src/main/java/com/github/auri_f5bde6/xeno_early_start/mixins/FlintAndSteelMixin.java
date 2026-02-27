package com.github.auri_f5bde6.xeno_early_start.mixins;

import com.github.auri_f5bde6.xeno_early_start.block.PrimitiveFireBlock;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.item.FireChargeItem;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({FlintAndSteelItem.class, FireChargeItem.class})
public class FlintAndSteelMixin {
    @Inject(method = "useOnBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;", shift = At.Shift.AFTER), cancellable = true)
    void cancelIfNoFuelTime(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        if (!PrimitiveFireBlock.canBeLit(context.getWorld().getBlockState(context.getBlockPos()), context)) {
            cir.setReturnValue(ActionResult.FAIL);
        }
    }
    @WrapOperation(method = "useOnBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/CampfireBlock;canBeLit(Lnet/minecraft/block/BlockState;)Z"))
    public boolean canBeLit(BlockState state, Operation<Boolean> original, @Local(argsOnly = true) ItemUsageContext context) {
        return original.call(state) || PrimitiveFireBlock.canBeLit(state, context);
    }
}
