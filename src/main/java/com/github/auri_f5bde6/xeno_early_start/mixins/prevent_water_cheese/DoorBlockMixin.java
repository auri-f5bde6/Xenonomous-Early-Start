package com.github.auri_f5bde6.xeno_early_start.mixins.prevent_water_cheese;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DoorBlock.class)
public abstract class DoorBlockMixin extends Block implements Waterloggable {
    @Shadow
    @Final
    public static EnumProperty<DoubleBlockHalf> HALF;
    @Unique
    private static BooleanProperty xeno_early_start$WATERLOGGED = Properties.WATERLOGGED;

    public DoorBlockMixin(Settings arg) {
        super(arg);
    }

    @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/state/StateManager;getDefaultState()Lnet/minecraft/state/State;"))
    public <O, S extends net.minecraft.state.State<O, S>> S setState(StateManager<O, S> instance, Operation<S> original) {
        return original.call(instance).with(xeno_early_start$WATERLOGGED, false);
    }

    @Inject(method = "appendProperties", at = @At("TAIL"))
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(xeno_early_start$WATERLOGGED);
    }

    @WrapOperation(method = "getPlacementState", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/DoorBlock;getDefaultState()Lnet/minecraft/block/BlockState;"))
    public BlockState addWaterLoggingCheck(DoorBlock instance, Operation<BlockState> original, ItemPlacementContext ctx) {
        var fluid = ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid();
        return original.call(instance).with(xeno_early_start$WATERLOGGED, fluid == Fluids.WATER);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(xeno_early_start$WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Inject(method = "getStateForNeighborUpdate", at = @At("HEAD"))
    public void getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos, CallbackInfoReturnable<BlockState> cir) {
        if (state.get(xeno_early_start$WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        var up = pos.up();
        var fluid = world.getFluidState(up).getFluid();
        world.setBlockState(up, state.with(xeno_early_start$WATERLOGGED, fluid == Fluids.WATER).with(HALF, DoubleBlockHalf.UPPER), Block.NOTIFY_ALL);
    }
}
