package net.hellomouse.xeno_early_start.mixins.prevent_water_cheese;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.FluidDrainable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.FluidModificationItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BucketItem.class)
public abstract class BucketItemMixin extends Item implements FluidModificationItem {
    public BucketItemMixin(Settings settings) {
        super(settings);
    }

    @Definition(id = "FluidDrainable", type = FluidDrainable.class)
    @Expression("? instanceof FluidDrainable")
    @Inject(method = "use", at = @At(value = "MIXINEXTRAS:EXPRESSION", shift = At.Shift.BEFORE), cancellable = true)
    public void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir,
                    @Local(type = BlockHitResult.class, ordinal = 0) BlockHitResult blockhitresult,
                    @Local(type = ItemStack.class, ordinal = 0) ItemStack itemstack) {
        if (user.isSubmergedInWater() && !world.getBlockState(blockhitresult.getBlockPos().up()).isAir()) {
            cir.setReturnValue(TypedActionResult.fail(itemstack));
        }
    }
}
