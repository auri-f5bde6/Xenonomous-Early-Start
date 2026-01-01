package com.github.auri_f5bde6.xeno_early_start.mixins;

import com.github.auri_f5bde6.xeno_early_start.block.PrimitiveFireBlock;
import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartBlockRegistry;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ShovelItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShovelItem.class)
public class ShovelItemMixin {
    @Inject(method = "useOnBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getToolModifiedState(Lnet/minecraft/item/ItemUsageContext;Lnet/minecraftforge/common/ToolAction;Z)Lnet/minecraft/block/BlockState;"), cancellable = true)
    void extinguishPrimitiveFire(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir, @Local World world, @Local BlockPos pos, @Local(ordinal = 0) BlockState state, @Local PlayerEntity player) {
        if (state.isOf(XenoEarlyStartBlockRegistry.PRIMITIVE_FIRE.get())) {
            world.setBlockState(pos, state.with(PrimitiveFireBlock.LIT, false));
            if (player != null && !player.isCreative()) {
                context.getStack().damage(1, player, p -> p.sendToolBreakStatus(context.getHand()));
            }
            cir.setReturnValue(ActionResult.success(world.isClient));
        }
    }
}
