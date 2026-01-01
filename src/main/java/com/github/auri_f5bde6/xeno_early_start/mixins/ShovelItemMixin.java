package com.github.auri_f5bde6.xeno_early_start.mixins;

import com.github.auri_f5bde6.xeno_early_start.block.PrimitiveFireBlock;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.item.ShovelItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ShovelItem.class)
public class ShovelItemMixin {
    @Definition(id = "CampfireBlock", type = CampfireBlock.class)
    @Definition(id = "getBlock", method = "Lnet/minecraft/block/BlockState;getBlock()Lnet/minecraft/block/Block;")
    @Expression("?.getBlock() instanceof CampfireBlock")
    @WrapOperation(method = "useOnBlock", at = @At("MIXINEXTRAS:EXPRESSION"))
    boolean extinguishPrimitiveFire(Object object, Operation<Boolean> original, @Local World world, @Local BlockPos pos, @Local(ordinal = 0) BlockState state) {
        if (object instanceof PrimitiveFireBlock) {
            world.setBlockState(pos, state.with(PrimitiveFireBlock.LIT, false));
        }
        return original.call(object);
    }
}
