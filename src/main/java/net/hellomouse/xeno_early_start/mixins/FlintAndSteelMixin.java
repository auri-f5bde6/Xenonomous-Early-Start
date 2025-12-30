package net.hellomouse.xeno_early_start.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.hellomouse.xeno_early_start.block.PrimitiveFireBlock;
import net.hellomouse.xeno_early_start.registries.ProgressionModBlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.state.property.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FlintAndSteelItem.class)
public class FlintAndSteelMixin {
    @WrapOperation(method = "useOnBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/CampfireBlock;canBeLit(Lnet/minecraft/block/BlockState;)Z"))
    public boolean canBeLit(BlockState state, Operation<Boolean> original) {
        return original.call(state) || PrimitiveFireBlock.canBeLit(state);
    }

    @WrapOperation(method = "useOnBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;with(Lnet/minecraft/state/property/Property;Ljava/lang/Comparable;)Ljava/lang/Object;"))
    Object resetLightLevel(BlockState instance, Property property, Comparable comparable, Operation<BlockState> original) {
        var state = original.call(instance, property, comparable);
        if (instance.isOf(ProgressionModBlockRegistry.PRIMITIVE_FIRE.get())) {
            state = state.with(PrimitiveFireBlock.LIGHT_LEVEL, 15);
        }
        return state;
    }
}
