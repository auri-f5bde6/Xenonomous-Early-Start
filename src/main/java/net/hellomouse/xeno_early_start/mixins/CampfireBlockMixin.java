package net.hellomouse.xeno_early_start.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.state.property.BooleanProperty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CampfireBlock.class)
public class CampfireBlockMixin {
    @Shadow
    @Final
    public static BooleanProperty LIT;

    @ModifyReturnValue(method = "getPlacementState",at = @At(value = "RETURN"))
    private BlockState modifyPlacementContext(BlockState original) {
        return original.with(LIT, false);
    }
}
