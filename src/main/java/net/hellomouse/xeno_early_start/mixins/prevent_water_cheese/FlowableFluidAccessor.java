package net.hellomouse.xeno_early_start.mixins.prevent_water_cheese;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FlowableFluid.class)
public interface FlowableFluidAccessor {
    @Invoker
    boolean callCanFlow(
            BlockView world,
            BlockPos fluidPos,
            BlockState fluidBlockState,
            Direction flowDirection,
            BlockPos flowTo,
            BlockState flowToBlockState,
            FluidState fluidState,
            Fluid fluid
    );
}
