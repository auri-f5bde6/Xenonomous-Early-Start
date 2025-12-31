package com.github.auri_f5bde6.xeno_early_start.mixins;

import com.github.auri_f5bde6.xeno_early_start.block.PrimitiveFireBlock;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.item.FlintAndSteelItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FlintAndSteelItem.class)
public class FlintAndSteelMixin {
    @WrapOperation(method = "useOnBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/CampfireBlock;canBeLit(Lnet/minecraft/block/BlockState;)Z"))
    public boolean canBeLit(BlockState state, Operation<Boolean> original) {
        return original.call(state) || PrimitiveFireBlock.canBeLit(state);
    }
}
