package com.github.auri_f5bde6.xeno_early_start.mixins.accessors;

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
    @Invoker("canFlow")
    boolean xeno_early_start$canFlow(
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
