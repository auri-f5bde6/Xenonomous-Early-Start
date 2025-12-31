package com.github.auri_f5bde6.xeno_early_start.mixins;

import com.github.auri_f5bde6.xeno_early_start.block.block_entity.BrickFurnaceBlockEntity;
import com.github.auri_f5bde6.xeno_early_start.mixins.accessors.AbstractFurnaceEntityAccessor;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

import static com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartDamageSource.FURNACE_EXPLOSIONS;
import static com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartDamageSource.getDamageSource;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin extends LockableContainerBlockEntity implements SidedInventory, RecipeUnlocker, RecipeInputProvider {

    protected AbstractFurnaceBlockEntityMixin(BlockEntityType<?> arg, BlockPos arg2, BlockState arg3) {
        super(arg, arg2, arg3);
    }

    @Inject(at = @At("HEAD"), method = "getCookTime(Lnet/minecraft/world/World;Lnet/minecraft/block/entity/AbstractFurnaceBlockEntity;)I", cancellable = true)
    private static void getCookTime(World world, AbstractFurnaceBlockEntity furnace, CallbackInfoReturnable<Integer> cir) {
        if (furnace instanceof BrickFurnaceBlockEntity) {
            cir.setReturnValue((furnace.matchGetter.getFirstMatch(furnace, world).map(AbstractCookingRecipe::getCookTime).orElse(200)) * 3);
        }
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/AbstractFurnaceBlockEntity;getMaxCountPerStack()I"))
    private static void tick(World world, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity blockEntity, CallbackInfo ci, @Local ItemStack itemstack) {
        var ingredient = ((AbstractFurnaceEntityAccessor) blockEntity).xeno_early_start$getInventory().get(0);
        var isFuel = ((AbstractFurnaceEntityAccessor) blockEntity).xeno_early_start$getFuelTime(itemstack) > 0;
        if (isFuel && ingredient.isOf(Items.TNT)) {
            ingredient.setCount(ingredient.getCount() - 1);
            itemstack.setCount(itemstack.getCount() - 1);
            world.createExplosion(null, getDamageSource(FURNACE_EXPLOSIONS, world.getRegistryManager()), new ExplosionBehavior(), Vec3d.of(pos), 3f, true, World.ExplosionSourceType.BLOCK);
        }
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/RecipeManager$MatchGetter;getFirstMatch(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/world/World;)Ljava/util/Optional;"))
    private static <C extends Inventory, T extends Recipe<C>> Optional<T> dontSmeltIron(RecipeManager.MatchGetter instance, C c, World world, Operation<Optional<T>> original) {
        var result = original.call(instance, c, world);
        if (c instanceof BrickFurnaceBlockEntity) {
            if (result.isPresent()) {
                var r = result.get();
                var stack = r.getOutput(world.getRegistryManager());
                if (stack.isOf(Items.IRON_INGOT) || stack.isOf(Items.IRON_NUGGET)) {
                    return Optional.empty();
                }
            }
        }
        return result;
    }
}
